package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.EventDispatcher;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andres Dominguez
 */
class ParentNamespaceFinder {

  private final Document document;
  private final EventDispatcher<ChangeListener> myEventDispatcher =
      EventDispatcher.create(ChangeListener.class);
  private final Project project;

  private String currentNamespace;
  private String parentNamespace;
  private List<Function> functionNames;

  public ParentNamespaceFinder(Document document, Project project) {
    this.document = document;
    this.project = project;
  }

  public String getCurrentNamespace() {
    return currentNamespace;
  }

  public String getParentNamespace() {
    return parentNamespace;
  }

  public List<Function> getFunctionNames() {
    return functionNames;
  }

  List<Document> findParents() {
    final ArrayList<Document> hierarchy = new ArrayList<Document>();

    ClassFinder finder = new ClassFinder(document);

    // Get the ns.
    currentNamespace = finder.getClassName();
    parentNamespace = finder.getParentClassName();

    if (currentNamespace == null || parentNamespace == null) {
      return hierarchy;
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

    return hierarchy;
  }

  private boolean fileContainsClass(VirtualFile virtualFile, String className) {
    if (!virtualFile.getName().endsWith(".js")) {
      return false;
    }

    Document document = getDocument(virtualFile);
    ClassFinder classFinder = new ClassFinder(document);

    return className.equals(classFinder.getClassName());
  }

  private void iterateFiles(ContentIterator iterator) {
    ProjectRootManager.getInstance(project).getFileIndex().iterateContent(iterator);
  }

  public void findParentClass() {
    ApplicationManager.getApplication().runReadAction(new Runnable() {
      @Override
      public void run() {
        functionNames = new ArrayList<Function>();

        List<Document> parents = findParents();
        for (Document doc : parents) {
          ClassFinder finder = new ClassFinder(doc);
          functionNames.addAll(finder.getMethods());
        }

        broadcastEvent();
      }
    });
  }

  private Document getDocument(VirtualFile virtualFile) {
    return FileDocumentManager.getInstance().getDocument(virtualFile);
  }

  private void broadcastEvent() {
    myEventDispatcher.getMulticaster().stateChanged(new ChangeEvent("ParentNamespaceFound"));
  }

  /**
   * Register for change events.
   *
   * @param changeListener The listener to be added.
   */
  public void addResultsReadyListener(ChangeListener changeListener) {
    myEventDispatcher.addListener(changeListener);
  }

}
