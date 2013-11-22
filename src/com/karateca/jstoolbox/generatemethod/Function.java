package com.karateca.jstoolbox.generatemethod;

/**
 * @author Andres Dominguez.
 */
public class Function {

  private final String name;
  private final String arguments;

  public Function(String name, String arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  public String getName() {
    return name;
  }

  public String getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return name;
  }
}
