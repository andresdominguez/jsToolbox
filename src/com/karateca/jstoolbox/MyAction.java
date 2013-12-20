package com.karateca.jstoolbox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;

/**
 * @author Andres Dominguez.
 */
public abstract class MyAction extends AnAction {
  @Override
  public void update(AnActionEvent e) {
    e.getPresentation().setEnabled(canEnableAction(e));
  }

  protected boolean canEnableAction(AnActionEvent e) {
    Editor editor = e.getData(PlatformDataKeys.EDITOR);
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);

    // Need the following objects.
    return editor != null && file != null && e.getProject() != null;
  }

  protected String getCurrentFileName(AnActionEvent e) {
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);
    return file == null ? "" : file.getName();
  }

  protected boolean isJsFile(AnActionEvent e) {
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);

    return file != null && file.getName().endsWith(".js");
  }

  /**
   * Run a write operation within a command.
   *
   * @param project The current project.
   * @param description The command description.
   * @param action The action to run.
   */
  protected void runWriteActionInsideCommand(Project project,
      String description, final Runnable action) {
    CommandProcessor.getInstance().executeCommand(project, new Runnable() {
      @Override
      public void run() {
        ApplicationManager.getApplication().runWriteAction(action);
      }
    }, description, null);
  }

  public LineRange getSelectedLineRange(Editor editor) {
    SelectionModel selectionModel = editor.getSelectionModel();
    VisualPosition startPosition = selectionModel.getSelectionStartPosition();
    VisualPosition endPosition = selectionModel.getSelectionEndPosition();

    if (startPosition == null || endPosition == null) {
      return null;
    }

    int startLine = startPosition.getLine();
    int endLine = endPosition.getLine();

    return new LineRange(Math.min(startLine, endLine), Math.max(startLine, endLine));
  }

  /**
   * Get the text range for a line of code.
   *
   * @param lineNumber The line number.
   * @param document
   * @return The text range for the line.
   */
  public TextRange getTextRange(int lineNumber, Document document) {
    int lineStart = document.getLineStartOffset(lineNumber);
    int lineEnd = document.getLineEndOffset(lineNumber);

    return new TextRange(lineStart, lineEnd);
  }

  protected String getLocForLineNumber(int lineNumber, Document document) {
    return document.getText(getTextRange(lineNumber, document));
  }

  protected int getLineNumberAtCaret(Editor editor, Document document) {
    int offset = editor.getCaretModel().getOffset();
    return document.getLineNumber(offset);
  }
}
