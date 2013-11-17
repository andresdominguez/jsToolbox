package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class GoToTestAction extends GoToRelatedAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    // Disable if dependencies are not met.
    if (!canEnableAction(e)) {
      return;
    }

    String fileName = getCurrentFileName(e);

    if (isTestFile(fileName)) {
      // From test to file.
      goToFile(e, testSuffix, fileSuffix);
    } else if (isViewFile(fileName)) {
      // From view to test.
      goToFile(e, viewSuffix, testSuffix);
    } else if (isFileFile(fileName)) {
      // From file to test.
      goToFile(e, fileSuffix, testSuffix);
    }
  }
}
