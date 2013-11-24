package com.karateca.jstoolbox.generatemethod;

/**
 * @author Andres Dominguez.
 */
public class Function {

  private static final String DEFAULT_JS_DOC = "/**\n" +
      " * @override\n" +
      " */";

  private final String name;
  private final String arguments;
  private final String jsDoc;

  public Function(String name, String arguments, String jsDoc) {
    this.name = name;
    this.arguments = arguments;
    this.jsDoc = jsDoc;
  }

  public String getName() {
    return name;
  }

  public String getArguments() {
    return arguments;
  }

  public String getJsDoc() {
    return jsDoc;
  }

  public String getExtendedJsDoc() {
    if (jsDoc == null) {
      return DEFAULT_JS_DOC;
    }

    // Do not add override twice.
    if (jsDoc.contains("@override")) {
      return jsDoc;
    }

    // Put the @override before the first param or at the end when
    // there aren't any params.
    String replace = jsDoc.contains("@param") ? " \\* @param" : " \\*/";

    return jsDoc.replaceFirst(replace, " * @override\n" + replace);
  }

  @Override
  public String toString() {
    return name;
  }
}
