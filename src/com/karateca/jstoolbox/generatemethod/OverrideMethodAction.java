package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.karateca.jstoolbox.MyAction;

import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andres Dominguez
 */
public class OverrideMethodAction extends MyAction {

  private Project project;
  private Editor editor;
  private Document document;
  private ParentNamespaceFinder namespaceFinder;

  @Override
  public void actionPerformed(AnActionEvent actionEvent) {
    if (!canEnableAction(actionEvent)) {
      return;
    }
    project = actionEvent.getData(PlatformDataKeys.PROJECT);
    editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    VirtualFile virtualFile = actionEvent
        .getData(PlatformDataKeys.VIRTUAL_FILE);
    document = editor.getDocument();

    namespaceFinder = new ParentNamespaceFinder(project, document, virtualFile, editor);

    namespaceFinder.addResultsReadyListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent changeEvent) {
        if (changeEvent.getSource().equals("ParentNamespaceFound")) {
          // TODO: show not found.
          showDialog();
        }
      }
    });

    namespaceFinder.findParentClass();
  }

  /**
   * Show the dialog to select the method to override.
   */
  private void showDialog() {
    List<Function> functionNames = namespaceFinder.getFunctionNames();

    // Select the closest element found from the current position.
    final JBList jbList = new JBList(functionNames.toArray());

    // Open a pop-up to select which describe() or it() you want to change.
    JBPopupFactory.getInstance()
        .createListPopupBuilder(jbList)
        .setTitle("Select the method to override")
        .setItemChoosenCallback(new Runnable() {
          public void run() {
            if (jbList.getSelectedValue() != null) {
              addNewMethod((Function) jbList.getSelectedValue());
            }
          }
        })
        .createPopup()
        .showCenteredInCurrentWindow(project);
  }

  /**
   * Create a new method that will override the parent.
   *
   * @param function The method that you want ot override.
   */
  private void addNewMethod(Function function) {
    String fnNameFormat = "%s.prototype." + function.getName();
    String methodPrototype = String
        .format(fnNameFormat, namespaceFinder.getCurrentNamespace());
    String parentMethodPrototype = String
        .format(fnNameFormat, namespaceFinder.getParentNamespace());

    String arguments = function.getArguments();
    String callArguments = arguments;
    if (callArguments.trim().length() > 0) {
      callArguments = ", " + callArguments;
    }

    final String methodTemplate = String.format("/**\n" +
        " * @override\n" +
        " */\n" +
        "%s = function(%s) {\n" +
        "  // TODO: override function\n" +
        "  %s.call(this%s);\n" +
        "};\n", methodPrototype, arguments, parentMethodPrototype,
        callArguments);

    runWriteActionInsideCommand(project, "Override method", new Runnable() {
      @Override
      public void run() {
        int offset = editor.getCaretModel().getOffset();
        document.replaceString(offset, offset, methodTemplate);
      }
    });
  }
}
