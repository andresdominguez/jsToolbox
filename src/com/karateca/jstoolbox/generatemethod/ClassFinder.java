package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassFinder {

  private final Document document;
  private final String documentText;
  // Constructor with this pattern: function MyConstructor() {}
  private static final Pattern FUNCTION_THEN_NAME_PATTERN =
      Pattern.compile("(\\s*function\\s+)([\\w\\.]+)\\s*.*");
  // Constructor with this pattern: MyConstructor = function () {}
  private static final Pattern NAME_THEN_FUNCTION_PATTERN =
      Pattern.compile("(\\s*)([\\w\\.]+)(\\s*=\\s*function.*)");

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

    // Determine the type of constructor:
    // 1) function MyConstructor() {}
    // 2) MyConstructor = function() {}
    int openingBraceIndex = documentText.indexOf("(", closingJsDocOffset);
    if (openingBraceIndex < 0) {
      return null;
    }

    String constructorSubstring = documentText.substring(
        closingJsDocOffset, openingBraceIndex).trim();

    boolean isNameThenFunctionPattern =
        constructorSubstring.matches("\\s*[\\w\\.]+\\s*=\\s*function.*");
    Pattern pattern = isNameThenFunctionPattern ?
        NAME_THEN_FUNCTION_PATTERN : FUNCTION_THEN_NAME_PATTERN;

    Matcher matcher = pattern.matcher(constructorSubstring);
    return matcher.find() ? matcher.group(2) : null;
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

  public List<Function> getSortedMethods() {
    List<Function> methods = getMethods();
    Collections.sort(methods, new Comparator<Function>() {
      @Override
      public int compare(Function left, Function right) {
        return left.getName().compareTo(right.getName());
      }
    });
    return methods;
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

      Function function = new FunctionBuilder()
          .setName(name)
          .setArguments(arguments)
          .setJsDoc(jsDoc)
          .setClassName(className)
          .createFunction();

      result.add(function);
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
