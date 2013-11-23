package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andres Dominguez.
 */
class HierarchyFinder {
  private final Project project;
  private final Document startingDocument;

  public HierarchyFinder(Project project, Document startingDocument) {
    this.project = project;
    this.startingDocument = startingDocument;
  }

  public HierarchyResults findParents() {
    final List<Document> hierarchy = new ArrayList<Document>();

    ClassFinder finder = new ClassFinder(startingDocument);

    // Get the ns.
    String currentNamespace = finder.getClassName();
    final String parentNamespace = finder.getParentClassName();

    if (currentNamespace == null || parentNamespace == null) {
      return null;
    }

    // Iterate through the project files until the parent is found.
    iterateFiles(new ContentIterator() {
      @Override
      public boolean processFile(VirtualFile fileOrDir) {
        boolean continueSearch = true;

        if (fileContainsClass(fileOrDir, parentNamespace)) {
          // Stop the search.
          continueSearch = false;
          hierarchy.add(getDocument(fileOrDir));
        }

        return continueSearch;
      }
    });

    return new HierarchyResults(hierarchy, parentNamespace, currentNamespace);
  }

  private void iterateFiles(ContentIterator iterator) {
    // TODO: find a way to iterate js files only.
    ProjectRootManager.getInstance(project).getFileIndex().iterateContent(iterator);
  }

  private boolean fileContainsClass(VirtualFile virtualFile, String className) {
    if (!virtualFile.getName().endsWith(".js")) {
      return false;
    }

    Document document = getDocument(virtualFile);
    ClassFinder classFinder = new ClassFinder(document);

    return className.equals(classFinder.getClassName());
  }

  protected Document getDocument(VirtualFile virtualFile) {
    return FileDocumentManager.getInstance().getDocument(virtualFile);
  }
}
