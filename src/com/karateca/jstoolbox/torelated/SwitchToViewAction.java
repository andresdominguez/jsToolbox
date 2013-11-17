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
public class SwitchToViewAction extends AnAction {

  public void actionPerformed(AnActionEvent e) {
  }

  @Override
  public void update(AnActionEvent e) {
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);
    Editor editor = e.getData(PlatformDataKeys.EDITOR);

    // Disable the option if there is no file or editor.
    if (file == null || editor == null) {
      e.getPresentation().setEnabled(false);
      return;
    }

    final String fileName = file.getName();

    JsToolboxSettings settings = new JsToolboxSettings();

    String fileSuffix = settings.getFileSuffix();
    String viewSuffix = settings.getViewSuffix();
    String testSuffix = settings.getTestSuffix();

    // Only process view and file type.
    boolean isJsFile = fileName.endsWith(fileSuffix);
    boolean isHtmlFile = fileName.endsWith(viewSuffix);

    if (!isJsFile && !isHtmlFile) {
      return;
    }

    String findFileName;
    if (isHtmlFile) {
      findFileName = fileName.replace(viewSuffix, fileSuffix);
    } else {
      String replace = fileSuffix;

      if (fileName.endsWith(testSuffix)) {
        replace = testSuffix;
      }

      findFileName = fileName.replace(replace, viewSuffix);
    }

    ContentIterator fileIterator = new FindRelatedFileIterator(findFileName, PsiManager.getInstance(
        editor.getProject()));

    ProjectRootManager.getInstance(editor.getProject()).getFileIndex().iterateContent(fileIterator);
  }
}
