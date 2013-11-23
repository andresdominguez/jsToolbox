package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.util.EventDispatcher;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andres Dominguez
 */
class NamespaceFinder {

  private final EventDispatcher<ChangeListener> myEventDispatcher =
      EventDispatcher.create(ChangeListener.class);
  private final Document document;

  private String namespaceFound;

  public NamespaceFinder(Document document) {
    this.document = document;
  }

  public void findNamespace() {
    ApplicationManager.getApplication().runReadAction(new Runnable() {
      @Override
      public void run() {
        namespaceFound = getClassName();

        // Did it find a namespace?
        if (namespaceFound != null) {
          myEventDispatcher.getMulticaster().stateChanged(
              new ChangeEvent("NamespaceFound"));
        }
      }
    });
  }

  private String getClassName() {
    ClassFinder classFinder = new ClassFinder(document);
    return classFinder.getClassName();
  }

  /**
   * Register for change events.
   *
   * @param changeListener The listener to be added.
   */
  public void addResultsReadyListener(ChangeListener changeListener) {
    myEventDispatcher.addListener(changeListener);
  }

  public String getNamespaceFound() {
    return namespaceFound;
  }
}
