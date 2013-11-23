package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;

public class ClassFinder {

  protected final Document document;
  private final String documentText;

  public ClassFinder(Document document) {
    this.document = document;
    documentText = document.getText();
  }

  public String getClassName() {
    // Find the @constructor and then go to the first function.
    int constructorOffset = documentText.indexOf("@constructor");
    if (constructorOffset < 0) {
      return null;
    }

    // Find the closing jsdoc.
    int closingJsDocOffset = documentText.indexOf("*/", constructorOffset);
    if (closingJsDocOffset < 0) {
      return null;
    }

    closingJsDocOffset += 2;

    // Get the string until the first "=".
    int indexOfEqual = documentText.indexOf("=", closingJsDocOffset);
    if (indexOfEqual < 0) {
      return null;
    }

    // And remove empty spaces.
    return documentText.substring(closingJsDocOffset, indexOfEqual)
        .replaceAll("\\s+", "");
  }

  public String getParentClassName() {
    // Find @extends.
    int extendsOffset = documentText.indexOf("@extends");
    if (extendsOffset < 0) {
      return null;
    }

    // Find the next "*".
    int lineNumber = document.getLineNumber(extendsOffset);
    String line = document.getText(getTextRange(lineNumber));

    // Remove the beginning of the line.
    line = line.replaceAll("[\\s\\*]*@extends\\s*\\{?", "");

    // Remove the end of the line.
    line = line.replaceAll("\\}?\\s*$", "");

    return line;
  }

  private TextRange getTextRange(int lineNumber) {
    int lineStart = document.getLineStartOffset(lineNumber);
    int lineEnd = document.getLineEndOffset(lineNumber);

    return new TextRange(lineStart, lineEnd);
  }
}
