package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.karateca.jstoolbox.MyAction;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class GenerateMethodAction extends MyAction {

  public void actionPerformed(AnActionEvent e) {
    if (!canEnableAction(e)) {
      return;
    }


  }
}
