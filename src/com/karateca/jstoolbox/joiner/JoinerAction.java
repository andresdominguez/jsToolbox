package com.karateca.jstoolbox.joiner;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.karateca.jstoolbox.MyAction;

/**
 * @author Andres Dominguez.
 */
public class JoinerAction extends MyAction {

  private static final String VAR_DECLARATION = "^\\s*var.*";
  private static final String MULTI_LINE_STRING = ".+\\+\\s*$";
  private static final String MULTI_LINE_STRING_SECOND_LINE = "^\\s*'.+";
  public static final String END_OF_MULTI_LINE_STRING = "'\\s*\\+\\s*$";
  public static final String BEGINNING_OF_MULTI_LINE_STRING = "^\\s*'";
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

    String currentLine = getLocForLineNumber(getLineNumberAtCaret());
    String nextLine = getNextLine();

    // Is the caret in a multi line string ('foo' +) and the next line is a
    // string?
    if (currentLine.matches(MULTI_LINE_STRING) &&
        nextLine.matches(MULTI_LINE_STRING_SECOND_LINE)) {
      if (isMultiLineSelection()) {
        joinSelectedLines();
      } else {
        joinStringWithNextLine();
      }
      return;
    }

    // Is it a variable declaration?
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

  private void joinSelectedLines() {
    SelectionModel selectionModel = editor.getSelectionModel();
    VisualPosition startPosition = selectionModel.getSelectionStartPosition();
    VisualPosition endPosition = selectionModel.getSelectionEndPosition();

    if (startPosition == null || endPosition == null) {
      return;
    }

    int startLine = startPosition.getLine();
    int endLine = endPosition.getLine();

    joinStringGivenLineRange(startLine, endLine);
  }

  private void joinStringGivenLineRange(int startLine, int endLine) {
    int startOffset = getTextRange(startLine).getStartOffset();
    int endOffset = getTextRange(endLine).getEndOffset();

    String textForRange = getTextForRange(new TextRange(startOffset, endOffset));

    String s = textForRange.replaceAll("'\\s*\\+\\s*'", "");

    replaceString(s, startOffset, endOffset);
  }

  private void joinStringWithNextLine() {
    int lineNumber = getLineNumberAtCaret();
    TextRange currentLineTextRange = getTextRange(lineNumber);

    // Get the text for the next line.
    TextRange nextLineTextRange = getTextRange(lineNumber + 1);
    String nextLine = getTextForRange(nextLineTextRange);

    // Merge the current line into the next line.
    String lineAtCaret = getLocForLineNumber(getLineNumberAtCaret());
    final String newLine = lineAtCaret.replaceAll(END_OF_MULTI_LINE_STRING, "") +
        nextLine.replaceAll(BEGINNING_OF_MULTI_LINE_STRING, "");

    final int start = currentLineTextRange.getStartOffset();
    final int end = nextLineTextRange.getEndOffset();

    replaceString(newLine, start, end);
  }

  private void replaceString(final String replacementText, final int start, final int end) {
    runWriteActionInsideCommand(new Runnable() {
      @Override
      public void run() {
        document.replaceString(start, end, replacementText);
      }
    });
  }

  private boolean isMultiLineSelection() {
    SelectionModel selectionModel = editor.getSelectionModel();
    return selectionModel.hasSelection() &&
        selectionModel.getSelectionStartPosition().getLine() !=
            selectionModel.getSelectionEndPosition().getLine();
  }

  private String getLocForLineNumber(int lineNumber) {
    return getTextForRange(getTextRange(lineNumber));
  }

  private String getNextLine() {
    int lineNumber = getLineNumberAtCaret();

    return getLocForLineNumber(lineNumber + 1);
  }

  private int getLineNumberAtCaret() {
    int offset = editor.getCaretModel().getOffset();
    return document.getLineNumber(offset);
  }

  private String getTextForRange(TextRange range) {
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
