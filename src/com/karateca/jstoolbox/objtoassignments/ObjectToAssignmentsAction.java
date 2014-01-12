package com.karateca.jstoolbox.objtoassignments;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.SelectionRange;
import com.karateca.jstoolbox.generatemethod.GenerateAction;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsAction extends GenerateAction {
  private Document document;
  private Project project;
  private static final Pattern endsWithSemicolon = Pattern.compile("^;\\n?");

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
//    int lineNumber = getLineNumberAtCaret(editor, document);
//    String currentLine = getLocForLineNumber(lineNumber, document);

    // Does the line starts with: var name = ?
//    if (!currentLine.matches("\\s*var\\s+\\w+\\s*=.*")) {
//      return;
//    }

    String documentText = document.getText();
    SelectionRange selectionRange = getSelectionRange(document, editor, documentText);

    if (selectionRange == null) {
      return;
    }

    String objectBlock = documentText.substring(selectionRange.start, selectionRange.end);

    ToAssignmentsConverter converter = new ToAssignmentsConverter(objectBlock);
    String assignments = converter.toAssignments();
    replaceString(assignments, selectionRange.start, selectionRange.end);
  }

  private SelectionRange getSelectionRange(Document document, Editor editor, String documentText) {
    // Is there any selection.
    SelectionModel selection = editor.getSelectionModel();
    if (selection.hasSelection()) {
      selection.getSelectionStart();
      selection.getSelectionEnd();

      return new SelectionRange(selection.getSelectionStart(), selection.getSelectionEnd());
    }

    // There is no selection. Find the object starting the the caret position.
    int lineNumberAtCaret = getLineNumberAtCaret(editor, document);
    int lineStartOffset = document.getLineStartOffset(lineNumberAtCaret);

    // Find the closing }.
    int closingBraceIndex = BraceMatcher.getClosingBraceIndex(documentText, lineStartOffset);
    if (closingBraceIndex == -1) {
      return null;
    }

    // Is the next character a ";"?
    String codeAfter = StringUtils.substring(documentText, closingBraceIndex);
    Matcher matcher = endsWithSemicolon.matcher(codeAfter);

    if (matcher.find()) {
      closingBraceIndex += matcher.end();
    }

    return new SelectionRange(lineStartOffset, closingBraceIndex);
  }

  private void replaceString(final String replacementText, final int start, final int end) {
    runWriteActionInsideCommand(project, "To assignments", new Runnable() {
      @Override
      public void run() {
        document.replaceString(start, end, replacementText);
      }
    });
  }
}
