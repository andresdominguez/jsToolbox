package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassFinder {

  private final Document document;
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

  public List<Function> getMethods() {
    List<Function> result = new ArrayList<Function>();
    int previousMatch = 0;

    String className = getClassName();

    String regexp = "(%s.prototype.)([\\w]+)(\\s*=\\s*function\\s*\\()" +
        "([\\w\\s\\n,]*)(\\))";
    String methodPattern = String.format(regexp, className);

    Pattern pattern = Pattern.compile(methodPattern, Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(documentText);

    while (matcher.find()) {
      String name = matcher.group(2);
      String arguments = matcher.group(4);

      // Find the jsdoc.
      String jsDoc = getJsDoc(previousMatch, matcher.start());
      previousMatch = matcher.start();

      result.add(new Function(name, arguments, jsDoc));
    }

    return result;
  }

  private String getJsDoc(int from, int to) {
    String substring = documentText.substring(from, to);

    int startOffset = substring.lastIndexOf("/**");
    if (startOffset < 0) {
      return null;
    }

    int endOffset = substring.lastIndexOf("*/");
    if (endOffset < 0) {
      return null;
    }

    // Add two characters for the closing tag "*/".
    endOffset += 2;

    // Is this the constructor?
    String jsDoc = substring.substring(startOffset, endOffset);
    if (from == 0 && jsDoc.contains("@constructor")) {
      return null;
    }

    return jsDoc;
  }
}
