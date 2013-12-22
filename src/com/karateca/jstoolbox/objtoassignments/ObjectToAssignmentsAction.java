package com.karateca.jstoolbox.objtoassignments;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.karateca.jstoolbox.LineRange;
import com.karateca.jstoolbox.generatemethod.GenerateAction;

import java.util.Stack;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsAction extends GenerateAction {
  private Document document;

  public void actionPerformed(AnActionEvent actionEvent) {
    Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (editor == null) {
      return;
    }
    document = editor.getDocument();

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

    String substring = documentText.substring(startOffset, closingBraceIndex + 1);
    System.out.println(substring);
  }

  private int getClosingBraceIndex(int startLine, String documentText) {
    TextRange startLineRange = getTextRange(startLine, document);
    int startOffset = startLineRange.getStartOffset();

    int documentEnd = documentText.length();
    Stack<Character> stack = new Stack<Character>();
    for (int i = startOffset; i < documentEnd; i++) {
      Character currentChar = documentText.charAt(i);
      if (currentChar == '{') {
        stack.push(currentChar);
      } else if (currentChar == '}') {
        if (stack.isEmpty()) {
          return -1;
        }

        stack.pop();

        if (stack.isEmpty()) {
          return i;
        }
      }
    }

    return -1;
  }
}
