package com.karateca.jstoolbox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiManager;
import com.karateca.jstoolbox.torelated.FindRelatedFileIterator;

/**
 * @author Andres Dominguez.
 */
public abstract class MyAction extends AnAction {
  @Override
  public void update(AnActionEvent e) {
    e.getPresentation().setEnabled(e.getData(PlatformDataKeys.EDITOR) != null);
  }

  protected void openFileInEditor(String findFileName, Project project) {
    ContentIterator fileIterator = new FindRelatedFileIterator(findFileName, PsiManager.getInstance(
        project));

    ProjectRootManager.getInstance(project).getFileIndex().iterateContent(fileIterator);
  }
}
