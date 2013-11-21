package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;

public class ClassFinder {

  protected String findNamespaceForCurrentFile(Document document) {
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
}
