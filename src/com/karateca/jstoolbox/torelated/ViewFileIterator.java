package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class ViewFileIterator implements ContentIterator {

  private final String fileName;
  private final PsiManager psiManager;
  private final boolean currentEditorFileIsView;

  public ViewFileIterator(PsiFile currentEditorFile, PsiManager psiManager) {
    this.fileName = currentEditorFile.getName();
    this.psiManager = psiManager;
    this.currentEditorFileIsView = currentEditorFile.getName().endsWith(".html");
  }

  public boolean processFile(VirtualFile virtualFile) {
    if (this.currentEditorFileIsView) {
      // Find the related file.
      if (fileName.replace(".html", ".js").equals(virtualFile.getName())) {
        psiManager.findFile(virtualFile).navigate(true);
      }
    } else {
      if (fileName.replace(".js", ".html").equals(virtualFile.getName())) {
        psiManager.findFile(virtualFile).navigate(true);
      }
    }

    return true;
  }
}
