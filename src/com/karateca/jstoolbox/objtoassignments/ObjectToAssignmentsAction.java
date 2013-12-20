package com.karateca.jstoolbox.objtoassignments;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.karateca.jstoolbox.LineRange;
import com.karateca.jstoolbox.generatemethod.GenerateAction;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsAction extends GenerateAction {
  private Editor editor;
  private Document document;

  public void actionPerformed(AnActionEvent actionEvent) {
    editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (editor == null) {
      return;
    }
    document = editor.getDocument();

    if (!this.canEnableAction(actionEvent)) {
      return;
    }

    // Find the var in the current line.
    LineRange selectedLineRange = getSelectedLineRange(editor);
    String currentLine = getLocForLineNumber(selectedLineRange.getStart(), document);

    // Does the line starts with: var name = ?
    if (!currentLine.matches("\\s*var\\s+\\w+\\s*=.*")) {
      return;
    }

    // Find the closing }.
    int closingBraceIndex = getClosingBraceIndex(selectedLineRange.getStart());
  }

  private int getClosingBraceIndex(int startLine) {
    return 0;  //To change body of created methods use File | Settings | File Templates.
  }
}
