package com.karateca.jstoolbox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.karateca.jstoolbox.torelated.FindRelatedFileIterator;

/**
 * @author Andres Dominguez.
 */
public abstract class MyAction extends AnAction {
  @Override
  public void update(AnActionEvent e) {
    e.getPresentation().setEnabled(canEnableAction(e));
  }

  public boolean canEnableAction(AnActionEvent e) {
    Editor editor = e.getData(PlatformDataKeys.EDITOR);
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);
    if (editor == null || file == null || e.getProject() == null) {

      return false;
    }
    return true;
  }

  protected void openFileInEditor(String findFileName, Project project) {
    ContentIterator fileIterator = new FindRelatedFileIterator(findFileName, PsiManager.getInstance(
        project));

    ProjectRootManager.getInstance(project).getFileIndex().iterateContent(fileIterator);
  }

  protected String getCurrentFileName(AnActionEvent e) {
    PsiFile file = e.getData(LangDataKeys.PSI_FILE);

    return file.getName();
  }

  protected void goToFile(AnActionEvent e, String fromSuffix, String toSuffix) {
    String fileName = e.getData(LangDataKeys.PSI_FILE).getName();

    String findFileName = fileName.replace(fromSuffix, toSuffix);

    openFileInEditor(findFileName, e.getProject());
  }
}
