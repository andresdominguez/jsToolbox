package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.karateca.jstoolbox.MyAction;

/**
 * @author Andres Dominguez.
 */
abstract class GenerateAction extends MyAction {
  @Override
  protected boolean canEnableAction(AnActionEvent e) {
    boolean canEnable = super.canEnableAction(e);

    return canEnable && isJsFile(e);
  }
}
