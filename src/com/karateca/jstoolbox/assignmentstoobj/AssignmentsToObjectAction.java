package com.karateca.jstoolbox.assignmentstoobj;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.generatemethod.GenerateAction;

/**
 * @author Andres Dominguez.
 */
public class AssignmentsToObjectAction extends GenerateAction {
  private Document document;
  private Project project;

  public void actionPerformed(AnActionEvent actionEvent) {
    Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (editor == null || !canEnableAction(actionEvent)) {
      return;
    }

    // No selection? Nothing to do here.
    SelectionModel selection = editor.getSelectionModel();
    if (!selection.hasSelection()) {
      return;
    }

    document = editor.getDocument();
    project = getEventProject(actionEvent);

    String selectedText = selection.getSelectedText();
    ToObjectConverter converter = new ToObjectConverter(selectedText);

    String objectDeclaration = converter.getObjectDeclaration();
    if (objectDeclaration == null) {
      return;
    }

    replaceString(objectDeclaration,
        selection.getSelectionStart(),
        selection.getSelectionEnd());
  }

  private void replaceString(final String replacementText, final int start, final int end) {
    runWriteActionInsideCommand(project, "To object", new Runnable() {
      @Override
      public void run() {
        document.replaceString(start, end, replacementText);
      }
    });
  }
}
