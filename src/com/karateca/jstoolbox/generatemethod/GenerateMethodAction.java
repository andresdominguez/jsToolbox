package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.MyAction;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andres Dominguez
 */
public class GenerateMethodAction extends MyAction {

  private Project project;
  private Editor editor;
  private Document document;
  private NamespaceFinder namespaceFinder;

  // TODO: Disable on non-js files.
  @Override
  public void actionPerformed(AnActionEvent actionEvent) {
    if (!canEnableAction(actionEvent)) {
      return;
    }

    project = actionEvent.getData(PlatformDataKeys.PROJECT);
    editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (project == null || editor == null) {
      return;
    }
    document = editor.getDocument();

    namespaceFinder = new NamespaceFinder(document);

    // Async callback to get the search results for it( and describe(
    namespaceFinder.addResultsReadyListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent changeEvent) {
        if (changeEvent.getSource().equals("NamespaceFound")) {
          String namespace = namespaceFinder.getNamespaceFound();

          // TODO: show error: "not found"
          if (namespace != null) {
            addMethod(namespace);
          }
        }
      }
    });

    namespaceFinder.findNamespace();
  }

  private void addMethod(final String namespace) {
    runWriteActionInsideCommand(project, "Added method", new Runnable() {
      @Override
      public void run() {
        int offset = editor.getCaretModel().getOffset();

        String methodTemplate = String.format("%s.prototype. = function() {\n" +
            "  \n" +
            "};", namespace);
        document.replaceString(offset, offset, methodTemplate);

        // Put the caret after "prototype."
        editor.getCaretModel().moveToOffset(offset + namespace.length() + 11);
      }
    });
  }
}
