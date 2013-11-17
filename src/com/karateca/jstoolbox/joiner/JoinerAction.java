package com.karateca.jstoolbox.joiner;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.karateca.jstoolbox.MyAction;

/**
 * @author Andres Dominguez.
 */
public class JoinerAction extends MyAction {

  private static final String VAR_DECLARATION = "^\\s*var.*";
  private Document document;
  private Editor editor;
  private Project project;

  public void actionPerformed(AnActionEvent actionEvent) {
    editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (editor == null) {
      return;
    }

    project = getEventProject(actionEvent);
    document = editor.getDocument();

    String currentLine = getLineAtCaret();

    // Does it end with "' +" ?
    if (currentLine.endsWith("+")) {
      joinStringWithNextLine();
      return;
    }

    // Is it a variable declaration?
    String nextLine = getNextLine();
    if (currentLine.endsWith(";") && nextLine.matches(VAR_DECLARATION)) {
      joinCurrentVariableDeclaration(currentLine);
    }
  }

  private void joinCurrentVariableDeclaration(final String currentLine) {
    final String nextLine = getNextLine();
    int lineNumber = getLineNumberAtCaret();
    final TextRange currentLineTextRange = getTextRange(lineNumber);
    final TextRange nextLineTextRange = getTextRange(lineNumber + 1);

    runWriteActionInsideCommand(new Runnable() {
      @Override
      public void run() {
        // Replace var from next line.
        String newLine = nextLine.replaceFirst("var", "   ");

        document.replaceString(nextLineTextRange.getStartOffset(),
            nextLineTextRange.getEndOffset(), newLine);

        // Replace ; from current line.
        newLine = currentLine.replaceAll(";$", ",");

        document.replaceString(currentLineTextRange.getStartOffset(),
            currentLineTextRange.getEndOffset(), newLine);
      }
    });
  }

  private void joinStringWithNextLine() {
    int lineNumber = getLineNumberAtCaret();
    TextRange currentLineTextRange = getTextRange(lineNumber);

    // Get the text for the next line.
    TextRange nextLineTextRange = getTextRange(lineNumber + 1);
    String nextLine = getLine(nextLineTextRange);

    // Merge the current line into the next line.
    final String newLine = getLineAtCaret().replaceAll("' \\+", "") +
        nextLine.replaceAll("^\\s*'", "");

    final int start = currentLineTextRange.getStartOffset();
    final int end = nextLineTextRange.getEndOffset();

    runWriteActionInsideCommand(new Runnable() {
      @Override
      public void run() {
        document.replaceString(start, end, newLine);
      }
    });
  }

  private String getLineAtCaret() {
    int lineNumber = getLineNumberAtCaret();

    return getLine(getTextRange(lineNumber));
  }

  private String getNextLine() {
    int lineNumber = getLineNumberAtCaret();

    return getLine(getTextRange(lineNumber + 1));
  }

  private int getLineNumberAtCaret() {
    int offset = editor.getCaretModel().getOffset();
    return document.getLineNumber(offset);
  }

  private String getLine(TextRange range) {
    return document.getText(range);
  }

  /**
   * Get the text range for a line of code.
   *
   * @param lineNumber The line number.
   * @return The text range for the line.
   */
  private TextRange getTextRange(int lineNumber) {
    int lineStart = document.getLineStartOffset(lineNumber);
    int lineEnd = document.getLineEndOffset(lineNumber);

    return new TextRange(lineStart, lineEnd);
  }

  /**
   * Run a write operation within a command.
   *
   * @param action The action to run.
   */
  private void runWriteActionInsideCommand(final Runnable action) {
    CommandProcessor.getInstance().executeCommand(project, new Runnable() {
      @Override
      public void run() {
        ApplicationManager.getApplication().runWriteAction(action);
      }
    }, "Join", null);
  }
}
