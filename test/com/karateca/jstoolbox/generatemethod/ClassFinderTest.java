package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

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

  public void testFindMethods() {
    // Given a file with functions.
    givenAFile("child.js");

    // When you get the functions.
    List<Function> methods = classFinder.getMethods();

    // Then ensure all the functions were found.
    assertEquals(4, methods.size());
    assertEquals("plainMethod", methods.get(0).getName());
    assertEquals("", methods.get(0).getArguments());

    assertEquals("withArguments", methods.get(1).getName());
    assertEquals("one, two", methods.get(1).getArguments());

    assertEquals("withManyLines", methods.get(2).getName());
    assertEquals("one, two,\n              three", methods.get(2).getArguments());

    assertEquals("withJsDoc", methods.get(3).getName());
    assertEquals("one, two, three", methods.get(3).getArguments());
  }

  private void givenAFile(String fileName) {
    prepareScenarioWithTestFile(fileName);
    classFinder = new ClassFinder(getDocument(virtualFile));
  }

  private Document getDocument(VirtualFile virtualFile) {
    return FileDocumentManager.getInstance().getDocument(virtualFile);
  }
}

