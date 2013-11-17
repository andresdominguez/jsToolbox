package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class SwitchToViewAction extends GoToRelatedAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    // Disable if dependencies are not met.
    if (!canEnableAction(e)) {
      return;
    }

    String fileName = getCurrentFileName(e);

    if (isViewFile(fileName)) {
      // Go from view to file.
      goToFile(e, viewSuffix, fileSuffix);
    } else if (isTestFile(fileName)) {
      // Go from test to view.
      goToFile(e, testSuffix, viewSuffix);
    } else if (isFileFile(fileName)) {
      // Go from file to view.
      goToFile(e, fileSuffix, viewSuffix);
    }
  }
}
