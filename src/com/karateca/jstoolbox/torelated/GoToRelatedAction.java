package com.karateca.jstoolbox.torelated;

import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;

/**
 * @author Andres Dominguez.
 */
abstract class GoToRelatedAction extends MyAction {

  final String fileSuffix;
  final String viewSuffix;
  final String testSuffix;

  GoToRelatedAction() {
    JsToolboxSettings settings = new JsToolboxSettings();

    fileSuffix = settings.getFileSuffix();
    viewSuffix = settings.getViewSuffix();
    testSuffix = settings.getTestSuffix();
  }

  boolean isFileFile(String fileName) {
    return fileName.endsWith(fileSuffix);
  }

  boolean isViewFile(String fileName) {
    return fileName.endsWith(viewSuffix);
  }

  boolean isTestFile(String fileName) {
    return fileName.endsWith(testSuffix);
  }
}
