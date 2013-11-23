package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Andres Dominguez.
 */
public class ClassFinderTest extends BaseTestCase {

  private ClassFinder classFinder;

  public void testGetClassNameClassFound() {
    // Given a file with a constructor.
    givenAFile("child.js");

    // When you get the class name.
    String className = classFinder.getClassName();

    // Then ensure the class name was found.
    assertEquals("Yo.child", className);
  }

  public void testGetClassNameClassNotFound() {
    // Given a file without constructor annotation.
    givenAFile("noConstructor.js");

    // When you get the class name.
    String className = classFinder.getClassName();

    // Then ensure the class name was not found.
    assertNull(className);
  }

  public void testGetParentClassName() {
    // Given a file with extends annotation.
    givenAFile("child.js");

    // When you get the parent class name.
    String parentClassName = classFinder.getParentClassName();

    // Then ensure the name was found.
    assertEquals("Yo.parent", parentClassName);
  }

  public void testGetParentClassNameNotFound() {
    // Given a class without a parent.
    givenAFile("grandpa.js");

    // When you get the parent class name.
    String parentClassName = classFinder.getParentClassName();

    // Then ensure it is null.
    assertNull(parentClassName);
  }

  private void givenAFile(String fileName) {
    prepareScenarioWithTestFile(fileName);
    classFinder = new ClassFinder(getDocument(virtualFile));
  }

  private Document getDocument(VirtualFile virtualFile) {
    return FileDocumentManager.getInstance().getDocument(virtualFile);
  }
}

