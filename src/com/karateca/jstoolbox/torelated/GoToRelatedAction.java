package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;

/**
 * @author Andres Dominguez.
 */
public abstract class GoToRelatedAction extends MyAction {

  private final String fileSuffix;
  final String viewSuffix;
  final String testSuffix;

  public GoToRelatedAction() {
    JsToolboxSettings settings = new JsToolboxSettings();

    fileSuffix = settings.getFileSuffix();
    viewSuffix = settings.getViewSuffix();
    testSuffix = settings.getTestSuffix();
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    // Disable if dependencies are not met.
    if (!canEnableAction(e)) {
      return;
    }

    String fileName = getCurrentFileName(e);

    String destinationSuffix = getDestinationSuffix();

    // Is it at destination? Go back to file.
    if (fileName.endsWith(destinationSuffix)) {
      goToFile(e, destinationSuffix, fileSuffix);
    } else if (isFileFile(fileName)) {
      // If I'm in file then go to destination.
      goToFile(e, fileSuffix, getDestinationSuffix());
    } else if (isViewFile(fileName)) {
      // Go from view to test.
      goToFile(e, viewSuffix, testSuffix);
    } else if (isTestFile(fileName)) {
      // Go from test to view.
      goToFile(e, testSuffix, viewSuffix);
    }
  }

  boolean isFileFile(String fileName) {
    return !isTestFile(fileName) && !isViewFile(fileName);
  }

  boolean isViewFile(String fileName) {
    return fileName.endsWith(viewSuffix);
  }

  boolean isTestFile(String fileName) {
    return fileName.endsWith(testSuffix);
  }

  abstract String getDestinationSuffix();
}
