package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
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

    final String fileName = getCurrentFileName(e);

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
