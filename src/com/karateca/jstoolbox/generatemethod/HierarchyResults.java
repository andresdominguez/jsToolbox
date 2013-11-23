package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;

import java.util.List;

/**
 * @author Andres Dominguez.
 */
public class HierarchyResults {
  private final List<Document> hierarchy;
  private final String parentClass;
  private final String currentClass;

  public HierarchyResults(List<Document> hierarchy, String parentClass, String currentClass) {
    this.hierarchy = hierarchy;
    this.parentClass = parentClass;
    this.currentClass = currentClass;
  }

  public List<Document> getHierarchy() {
    return hierarchy;
  }

  public String getParentClass() {
    return parentClass;
  }

  public String getCurrentClass() {
    return currentClass;
  }
}
