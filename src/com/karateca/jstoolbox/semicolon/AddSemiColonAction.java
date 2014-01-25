package com.karateca.jstoolbox.semicolon;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.MyAction;

public class AddSemiColonAction extends MyAction {

  public void actionPerformed(AnActionEvent actionEvent) {
    Editor editor = actionEvent.getData(PlatformDataKeys.EDITOR);
    if (editor == null) {
      return;
    }

    Project project = getEventProject(actionEvent);
    final Document document = editor.getDocument();
    final int lineEnd = editor.getCaretModel().getVisualLineEnd() - 1;

    runWriteActionInsideCommand(project, "Add semicolon", new Runnable() {
      @Override
      public void run() {
        document.replaceString(lineEnd, lineEnd, ";");
      }
    });
  }
}
