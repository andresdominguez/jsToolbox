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

    // Only process html and js.
    boolean isJsFile = fileName.endsWith(".js");
    boolean isHtmlFile = fileName.endsWith(".html");

    if (!isJsFile && !isHtmlFile) {
      return;
    }

    String findFileName;
    if (isHtmlFile) {
      findFileName = fileName.replace(".html", ".js");
    } else {
      String replace = ".js";
      if (fileName.endsWith("-spec.js")) {
        replace = "-spec.js";
      }

      findFileName = fileName.replace(replace, ".html");
    }

    ContentIterator fileIterator = new FindRelatedFileIterator(findFileName, PsiManager.getInstance(
        editor.getProject()));

    ProjectRootManager.getInstance(editor.getProject()).getFileIndex().iterateContent(fileIterator);
  }
}
