package com.karateca.jstoolbox.objtoassignments;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.karateca.jstoolbox.LineRange;
import com.karateca.jstoolbox.generatemethod.GenerateAction;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsAction extends GenerateAction {
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

    // Find the var in the current line.
    LineRange selectedLineRange = getSelectedLineRange(editor);
    int startOffset = selectedLineRange.getStart();
    String currentLine = getLocForLineNumber(startOffset, document);

    // Does the line starts with: var name = ?
    if (!currentLine.matches("\\s*var\\s+\\w+\\s*=.*")) {
      return;
    }

    // Find the closing }.
    String documentText = document.getText();
    int closingBraceIndex = getClosingBraceIndex(startOffset, documentText);
    if (closingBraceIndex == -1) {
      return;
    }

    String objectBlock = documentText.substring(startOffset, closingBraceIndex);
    ToAssignmentsConverter converter = new ToAssignmentsConverter(objectBlock);
    String assignments = converter.toAssignments();
    replaceString(assignments, startOffset, closingBraceIndex );
  }

  private void replaceString(final String replacementText, final int start, final int end) {
    runWriteActionInsideCommand(project, "To assignments", new Runnable() {
      @Override
      public void run() {
        document.replaceString(start, end, replacementText);
      }
    });
  }

  private int getClosingBraceIndex(int startLine, String documentText) {
    TextRange startLineRange = getTextRange(startLine, document);
    int startOffset = startLineRange.getStartOffset();

    return BraceMatcher.getClosingBraceIndex(documentText, startOffset);
  }
}
