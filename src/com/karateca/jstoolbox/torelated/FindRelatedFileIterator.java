package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;

import java.util.ArrayList;
import java.util.List;

class FindRelatedFileIterator implements ContentIterator {

  private final String fileName;
  private final PsiManager psiManager;
  private final List<PsiFile> files;

  public FindRelatedFileIterator(String fileName, PsiManager psiManager) {
    this.fileName = fileName;
    this.psiManager = psiManager;
    files = new ArrayList<>();
  }

  public List<PsiFile> getFiles() {
    return files;
  }

  public boolean processFile(VirtualFile virtualFile) {
    if (fileName.equals(virtualFile.getName())) {
      PsiFile file = psiManager.findFile(virtualFile);
      if (file != null) {
        files.add(file);
      }
    }
    return true;
  }
}
