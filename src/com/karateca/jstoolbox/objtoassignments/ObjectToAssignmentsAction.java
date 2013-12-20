package com.karateca.jstoolbox.objtoassignments;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.karateca.jstoolbox.generatemethod.GenerateAction;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsAction extends GenerateAction {
  public void actionPerformed(AnActionEvent e) {
    if (!this.canEnableAction(e)) {
      return;
    }

    // Find the var in the current line.

  }
}
