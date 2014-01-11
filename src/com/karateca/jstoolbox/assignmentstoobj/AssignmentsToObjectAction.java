package com.karateca.jstoolbox.assignmentstoobj;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.LineRange;
import com.karateca.jstoolbox.generatemethod.GenerateAction;

/**
 * @author Andres Dominguez.
 */
public class AssignmentsToObjectAction extends GenerateAction {
  private Document document;
  private Project project;

  public void actionPerformed(AnActionEvent actionEvent) {
    Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (editor == null) {
      return;
    }
    document = editor.getDocument();
    project = getEventProject(actionEvent);

    if (!this.canEnableAction(actionEvent)) {
      return;
    }

    LineRange lineRange = getSelectedLineRange(editor);
    if (lineRange.getStart() == lineRange.getEnd()) {
      return;
    }

    int selectionStart = document.getLineStartOffset(lineRange.getStart());
    int selectionEnd = document.getLineEndOffset(lineRange.getEnd());

    String documentText = document.getText();
    String selectedText = documentText.substring(selectionStart, selectionEnd);

    System.out.println(selectedText);
    ToObjectConverter converter = new ToObjectConverter(selectedText);

    String objectDeclaration = converter.getObjectDeclaration();
    if (objectDeclaration == null) {
      return;
    }
    replaceString(objectDeclaration, selectionStart, selectionEnd);
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
