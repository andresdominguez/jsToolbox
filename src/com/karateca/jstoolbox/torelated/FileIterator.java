package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

/**
 * @author andresdom@google.com (Andres Dominguez)
 */
public class FileIterator implements ContentIterator {

  private final String fileName;
  private final PsiManager psiManager;
  private final boolean currentEditorFileIsTest;

  public FileIterator(PsiFile currentEditorFile, PsiManager psiManager) {
    this.fileName = currentEditorFile.getName();
    this.psiManager = psiManager;
    this.currentEditorFileIsTest = currentEditorFile.getName().endsWith(
        "-spec.js");
  }

  public boolean processFile(VirtualFile virtualFile) {
    if (this.currentEditorFileIsTest) {
      // Find the related file.
      if (fileName.replace("-spec.js", ".js").equals(virtualFile.getName())) {
        psiManager.findFile(virtualFile).navigate(true);
      }
    } else {
      if (fileName.replace(".js", "-spec.js").equals(virtualFile.getName())) {
        psiManager.findFile(virtualFile).navigate(true);
      }
    }

    return true;
  }
}
