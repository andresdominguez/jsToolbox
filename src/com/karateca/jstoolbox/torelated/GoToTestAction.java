package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;

import static com.karateca.jstoolbox.torelated.CandidateFinder.suggestDestinationFiles;

public class GoToTestAction extends GoToRelatedAction {

  protected void performSwitch(AnActionEvent e, String fileName) {
    if (isTestFile(fileName)) {
      goToFiles(e, suggestDestinationFiles(fileName, testSuffixList, fileSuffixList));
    } else if (isViewFile(fileName)) {
      goToFiles(e, suggestDestinationFiles(fileName, viewSuffixList, testSuffixList));
    } else if (isInCodeFile(fileName)) {
      goToFiles(e, suggestDestinationFiles(fileName, fileSuffixList, testSuffixList));
    }
  }
}
