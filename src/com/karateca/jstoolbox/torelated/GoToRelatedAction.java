package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.karateca.jstoolbox.config.JsToolboxSettings;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class GoToRelatedAction extends AnAction {

  public void actionPerformed(AnActionEvent e) {
  }

  @Override
  public void update(AnActionEvent e) {
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);
    Editor editor = e.getData(PlatformDataKeys.EDITOR);

    // Disable the option if there is no file or editor.
    if (file == null || editor == null || editor.getProject() == null) {
      e.getPresentation().setEnabled(false);
      return;
    }

    JsToolboxSettings settings = new JsToolboxSettings();

    final String fileName = file.getName();

    // Ignore non-js files.
    String fileSuffix = settings.getFileSuffix();
    String testSuffix = settings.getTestSuffix();

    if (!fileName.endsWith(fileSuffix)) {
      return;
    }

    String findFileName;
    if (fileName.endsWith(testSuffix)) {
      findFileName = fileName.replace(testSuffix, fileSuffix);
    } else {
      findFileName = fileName.replace(fileSuffix, testSuffix);
    }

    ContentIterator fileIterator = new FindRelatedFileIterator(findFileName, PsiManager.getInstance(
        editor.getProject()));

    ProjectRootManager.getInstance(editor.getProject()).getFileIndex().iterateContent(fileIterator);
  }
}
