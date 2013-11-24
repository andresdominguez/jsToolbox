package com.karateca.jstoolbox.generatemethod;

/**
 * @author Andres Dominguez.
 */
public class Function {

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
    return null;
  }

  @Override
  public String toString() {
    return name;
  }
}
