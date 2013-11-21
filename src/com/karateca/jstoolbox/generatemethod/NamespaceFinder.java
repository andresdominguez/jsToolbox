package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.util.EventDispatcher;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Andres Dominguez
 */
public class NamespaceFinder extends ClassFinder {

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
        namespaceFound = findNamespaceForCurrentFile(
            NamespaceFinder.this.document);

        // Did it find a namespace?
        if (namespaceFound != null) {
          myEventDispatcher.getMulticaster().stateChanged(
              new ChangeEvent("NamespaceFound"));
        }
      }
    });
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
