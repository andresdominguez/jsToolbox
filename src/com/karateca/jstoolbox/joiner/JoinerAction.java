package com.karateca.jstoolbox.joiner;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.karateca.jstoolbox.LineRange;
import com.karateca.jstoolbox.MyAction;

/**
 * @author Andres Dominguez.
 */
public class JoinerAction extends MyAction {

  private static final String VAR_DECLARATION = "^\\s*var.*";
  private static final String MULTI_LINE_STRING = ".+\\+\\s*$";
  private static final String MULTI_LINE_STRING_SECOND_LINE = "^\\s*['\"].+";
  private static final String ENDS_WITH_SEMICOLON = ".*;\\s*$";
  public static final String singleQuotesSplitSeparator = "'\\s*\\+\\s*'";
  public static final String doubleQuotesSplitSeparator = "\"\\s*\\+\\s*\"";
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

    LineRange lineRange = getSelectedLineRange(editor);
    int firstSelectedLine = lineRange.getStart();

    String firstLine = getLocForLineNumber(firstSelectedLine, document);
    String nextLine = getLocForLineNumber(firstSelectedLine + 1, document);

    // Is the caret in a multi line string ('foo' +) and the next line is a string?
    if (firstLine.matches(MULTI_LINE_STRING) &&
        nextLine.matches(MULTI_LINE_STRING_SECOND_LINE)) {
      joinMultiLineString(lineRange);
      return;
    }

    // Is it a variable declaration?
    if (firstLine.matches(ENDS_WITH_SEMICOLON) && nextLine.matches(VAR_DECLARATION)) {
      joinCurrentVariableDeclaration(firstLine);
    }
  }

  private void joinMultiLineString(LineRange lineRange) {
    // Join either one line with the next or multiple lines.
    int endLine = Math.max(lineRange.getEnd(), lineRange.getStart() + 1);
    joinStringGivenLineRange(lineRange.getStart(), endLine);
  }

  private void joinCurrentVariableDeclaration(final String currentLine) {
    final String nextLine = getNextLine();
    int lineNumber = getLineNumberAtCaret(editor, document);
    final TextRange currentLineTextRange = getTextRange(lineNumber, document);
    final TextRange nextLineTextRange = getTextRange(lineNumber + 1, document);

    runWriteActionInsideCommand(project, "Join", new Runnable() {
      @Override
      public void run() {
        // Replace var from next line.
        String newLine = nextLine.replaceFirst("var", "   ");

        document.replaceString(nextLineTextRange.getStartOffset(),
            nextLineTextRange.getEndOffset(), newLine);

        // Replace ; from current line.
        newLine = currentLine.replaceAll(";\\s*$", ",");

        document.replaceString(
            currentLineTextRange.getStartOffset(),
            currentLineTextRange.getEndOffset(),
            newLine);
      }
    });
  }

  private void joinStringGivenLineRange(int startLine, int endLine) {
    TextRange startLineRange = getTextRange(startLine, document);
    int startOffset = startLineRange.getStartOffset();
    int endOffset = getTextRange(endLine, document).getEndOffset();

    String textForRange = getTextForRange(new TextRange(startOffset, endOffset));

    String firstLine = getTextForRange(startLineRange);
    String splitSeparator = firstLine.matches(".+'\\s*\\+\\s*") ?
        singleQuotesSplitSeparator :
        doubleQuotesSplitSeparator;

    // Remove the ' + \n ' in order to join.
    String s = textForRange.replaceAll(splitSeparator, "");

    replaceString(s, startOffset, endOffset);
  }

  private void replaceString(final String replacementText, final int start, final int end) {
    runWriteActionInsideCommand(project, "Join", new Runnable() {
      @Override
      public void run() {
        document.replaceString(start, end, replacementText);
      }
    });
  }

  private String getNextLine() {
    int lineNumber = getLineNumberAtCaret(editor, document);

    return getLocForLineNumber(lineNumber + 1, document);
  }

  private String getTextForRange(TextRange range) {
    return document.getText(range);
  }
}
