package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;

import static com.karateca.jstoolbox.torelated.CandidateFinder.suggestDestinationFiles;

public class GoToViewAction extends GoToRelatedAction {

  protected void performSwitch(AnActionEvent e, String fileName) {
    if (isViewFile(fileName)) {
      goToFiles(e, suggestDestinationFiles(fileName, viewSuffixList, fileSuffixList));
    } else if (isTestFile(fileName)) {
      goToFiles(e, suggestDestinationFiles(fileName, testSuffixList, viewSuffixList));
    } else if (isInCodeFile(fileName)) {
      goToFiles(e, suggestDestinationFiles(fileName, fileSuffixList, viewSuffixList));
    }

  }
}
