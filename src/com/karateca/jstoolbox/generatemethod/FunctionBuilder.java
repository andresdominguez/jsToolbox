package com.karateca.jstoolbox.generatemethod;

public class FunctionBuilder {
  private String name;
  private String arguments;
  private String jsDoc;
  private String className;

  public FunctionBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public FunctionBuilder setArguments(String arguments) {
    this.arguments = arguments;
    return this;
  }

  public FunctionBuilder setJsDoc(String jsDoc) {
    this.jsDoc = jsDoc;
    return this;
  }

  public FunctionBuilder setClassName(String className) {
    this.className = className;
    return this;
  }

  public Function createFunction() {
    return new Function(name, arguments, jsDoc, className);
  }
}
