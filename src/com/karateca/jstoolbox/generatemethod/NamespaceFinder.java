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
        namespaceFound = performFind();

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

  private String performFind() {
    String text = document.getText();

    // Find the @constructor and then go to the first function.
    int constructorOffset = text.indexOf("@constructor");
    if (constructorOffset < 0) {
      return null;
    }

    // Find the closing jsdoc.
    int closingJsDocOffset = text.indexOf("*/", constructorOffset);
    if (closingJsDocOffset < 0) {
      return null;
    }

    closingJsDocOffset += 2;

    // Get the string until the first "=".
    int indexOfEqual = text.indexOf("=", closingJsDocOffset);
    if (indexOfEqual < 0) {
      return null;
    }

    // And remove empty spaces.
    return text.substring(closingJsDocOffset, indexOfEqual)
        .replaceAll("\\s+", "");
  }

  public String getNamespaceFound() {
    return namespaceFound;
  }
}
