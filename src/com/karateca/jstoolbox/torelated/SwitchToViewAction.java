package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class SwitchToViewAction extends MyAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    // Disable if dependencies are not met.
    if (!canEnableAction(e)) {
      return;
    }

    String fileName = getCurrentFileName(e);

    JsToolboxSettings settings = new JsToolboxSettings();
    String fileSuffix = settings.getFileSuffix();
    String viewSuffix = settings.getViewSuffix();
    String testSuffix = settings.getTestSuffix();

    if (fileName.endsWith(viewSuffix)) {
      // Go from view to file.
      goToFile(e, viewSuffix, fileSuffix);
    } else if (fileName.endsWith(testSuffix)) {
      // Go from test to view.
      goToFile(e, testSuffix, viewSuffix);
    } else if (fileName.endsWith(fileSuffix)) {
      // Go from file to view.
      goToFile(e, fileSuffix, viewSuffix);
    }
  }
}
