package com.karateca.jstoolbox.assignmentstoobj;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.generatemethod.GenerateAction;

/**
 * @author Andres Dominguez.
 */
public class AssignmentsToObjectAction extends GenerateAction {
  private Document document;
  private Project project;

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
  }
}
