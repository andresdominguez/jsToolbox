package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiFile;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class GoToTestAction extends MyAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);

    // Disable if dependencies are not met.
    if (!canEnableAction(e)) {
      return;
    }

    final String fileName = file.getName();

    JsToolboxSettings settings = new JsToolboxSettings();
    String fileSuffix = settings.getFileSuffix();
    String testSuffix = settings.getTestSuffix();
    String viewSuffix = settings.getViewSuffix();

    // Ignore non-js files.
    if (fileName.endsWith(testSuffix)) {
      // From test to file.
      goToFile(e, testSuffix, fileSuffix);
    } else if (fileName.endsWith(viewSuffix)) {
      // From view to test.
      goToFile(e, viewSuffix, testSuffix);
    } else if (fileName.endsWith(fileSuffix)) {
      // From file to test.
      goToFile(e, fileSuffix, testSuffix);
    }
  }
}
