package com.karateca.jstoolbox.dashcamel;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.MyAction;

public class ToggleCamelDashCaseAction extends MyAction {
  @Override
  protected boolean canEnableAction(AnActionEvent e) {
    return super.canEnableAction(e) && getEditor(e).getSelectionModel().hasSelection();
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    Editor editor = getEditor(e);
    if (editor == null) {
      return;
    }

    final SelectionModel selectionModel = editor.getSelectionModel();
    String selectedText = selectionModel.getSelectedText();
    if (selectedText == null) {
      return;
    }

    Project project = e.getProject();

    final String camelCaseString = changeCase(selectedText);
    final Document document = editor.getDocument();
    runWriteActionInsideCommand(project, "Change case", new Runnable() {
      @Override
      public void run() {
        document.replaceString(
            selectionModel.getSelectionStart(),
            selectionModel.getSelectionEnd(),
            camelCaseString
        );
      }
    });
  }

  String changeCase(String selectedText) {
    return CaseHelper.toggleCase(selectedText);
  }
}
