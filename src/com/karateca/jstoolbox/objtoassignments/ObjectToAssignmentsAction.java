package com.karateca.jstoolbox.objtoassignments;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
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
  private static final Pattern endsWithSemicolon = Pattern.compile("^;\\n+");

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
    int lineNumberAtCaret = getLineNumberAtCaret(editor, document);
    String currentLine = getLocForLineNumber(lineNumberAtCaret, document);

    // Does the line starts with: var name = ?
    if (!currentLine.matches("\\s*var\\s+\\w+\\s*=.*")) {
      return;
    }

    int offsetAtStartOfCurrentLine = document.getLineStartOffset(lineNumberAtCaret);

    // Find the closing }.
    String documentText = document.getText();
    int closingBraceIndex = BraceMatcher.getClosingBraceIndex(documentText, offsetAtStartOfCurrentLine);
    if (closingBraceIndex == -1) {
      return;
    }

    String objectBlock = documentText.substring(offsetAtStartOfCurrentLine, closingBraceIndex);

    // Is the next character a ";"?
    String codeAfter = StringUtils.substring(documentText, closingBraceIndex);
    Matcher matcher = endsWithSemicolon.matcher(codeAfter);
    if (matcher.find()) {
      closingBraceIndex += matcher.end();
    }

    ToAssignmentsConverter converter = new ToAssignmentsConverter(objectBlock);
    String assignments = converter.toAssignments();
    replaceString(assignments, offsetAtStartOfCurrentLine, closingBraceIndex);
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
