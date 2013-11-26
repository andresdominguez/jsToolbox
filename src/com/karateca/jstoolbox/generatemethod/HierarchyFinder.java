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
  // The maximum number of files visited before giving up.
  private static final int MAX_HIERARCHY_DEPTH = 10;
  private final Project project;
  private final Document startingDocument;

  public HierarchyFinder(Project project, Document startingDocument) {
    this.project = project;
    this.startingDocument = startingDocument;
  }

  public HierarchyResults findParents() {
    final List<Document> hierarchyChain = new ArrayList<Document>();

    ClassFinder finder = new ClassFinder(startingDocument);
    String currentNamespace = finder.getClassName();
    String parentNamespace = finder.getParentClassName();

    if (currentNamespace == null || parentNamespace == null) {
      return null;
    }

    Document document = startingDocument;

    // Go up the chain until no more parents are found.
    int depth = 0;
    while (true) {
      // Avoid circular dependencies.
      if (depth++ >= MAX_HIERARCHY_DEPTH) {
        break;
      }

      finder = new ClassFinder(document);
      String parentClassName = finder.getParentClassName();
      if (parentClassName == null) {
        break;
      }

      document = findFileDeclaringClass(parentClassName);
      if (document == null) {
        break;
      }
      hierarchyChain.add(document);
    }

    return new HierarchyResults(hierarchyChain, parentNamespace, currentNamespace);
  }

  /**
   * Iterate through the project files until the file declaring the namespace is found.
   *
   * @param namespace The class name you are looking for.
   * @return The document declaring the namespace.
   */
  private Document findFileDeclaringClass(final String namespace) {
    final List<Document> hierarchy = new ArrayList<Document>();

    iterateFiles(new ContentIterator() {
      @Override
      public boolean processFile(VirtualFile fileOrDir) {
        boolean continueSearch = true;

        if (fileContainsClass(fileOrDir, namespace)) {
          // Stop the search.
          continueSearch = false;
          hierarchy.add(getDocument(fileOrDir));
        }

        return continueSearch;
      }
    });

    return hierarchy.size() > 0 ? hierarchy.get(0) : null;
  }

  private void iterateFiles(ContentIterator iterator) {
    // TODO: find a way to iterate js files only.
    ProjectRootManager.getInstance(project).getFileIndex().iterateContent(iterator);
  }

  private boolean fileContainsClass(VirtualFile virtualFile, String className) {
    // Some directory names can end with .js
    if (virtualFile.isDirectory() || !virtualFile.getName().endsWith(".js")) {
      return false;
    }

    Document document = getDocument(virtualFile);
    if (document == null) {
      return false;
    }
    ClassFinder classFinder = new ClassFinder(document);

    return className.equals(classFinder.getClassName());
  }

  protected Document getDocument(VirtualFile virtualFile) {
    return FileDocumentManager.getInstance().getDocument(virtualFile);
  }
}
