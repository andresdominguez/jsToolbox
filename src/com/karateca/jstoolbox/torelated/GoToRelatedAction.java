package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;

import java.util.Arrays;
import java.util.List;

/**
 * @author Andres Dominguez.
 */
public abstract class GoToRelatedAction extends MyAction {

  private String fileSuffix;
  String viewSuffix;
  String testSuffix;
  List<String> testSuffixList;

  @Override
  public void actionPerformed(AnActionEvent e) {
    // Disable if dependencies are not met.
    if (!canEnableAction(e)) {
      return;
    }

    readConfig();

    String fileName = getCurrentFileName(e);

    // Is it at destination? Go back to file.
    if (isInDestinationFile(fileName)) {
      goToFile(e, getDestinationMatch(fileName), fileSuffix);
    } else if (isFileFile(fileName)) {
      // If I'm in file then go to destination.
      goToFiles(e, fileSuffix, getDestinationSuffixList());
    } else if (isViewFile(fileName)) {
      // Go from view to test.
      goToFile(e, viewSuffix, testSuffix);
    } else if (isTestFile(fileName)) {
      // Go from test to view.
      goToFile(e, testSuffix, viewSuffix);
    }
  }

  private String getDestinationMatch(String fileName) {
    for (String suffix : getDestinationSuffixList()) {
      if (fileName.endsWith(suffix)) {
        return suffix;
      }
    }
    return null;
  }

  private void readConfig() {
    JsToolboxSettings settings = new JsToolboxSettings();

    testSuffix = null;
    testSuffixList = null;

    fileSuffix = settings.getFileSuffix();
    viewSuffix = settings.getViewSuffix();
    testSuffix = settings.getTestSuffix();
    if (testSuffix.contains(",")) {
      testSuffixList = Arrays.asList(testSuffix.split(","));
    }
  }

  boolean isFileFile(String fileName) {
    return !isTestFile(fileName) && !isViewFile(fileName);
  }

  boolean isViewFile(String fileName) {
    return fileName.endsWith(viewSuffix);
  }

  boolean isTestFile(String fileName) {
    return endsWithAnyOf(fileName, testSuffixList);
  }

  boolean isInDestinationFile(String fileName) {
    return endsWithAnyOf(fileName, getDestinationSuffixList());
  }

  private boolean endsWithAnyOf(String fileName, List<String> destinationSuffix) {
    for (String suffix : destinationSuffix) {
      if (fileName.endsWith(suffix)) {
        return true;
      }
    }
    return false;
  }

  abstract List<String> getDestinationSuffixList();
}
