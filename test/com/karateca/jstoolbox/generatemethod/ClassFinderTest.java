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

  public void testGetConstructorWhenNotFirstInFile() {
    // Given a file with a constructor not at the beginning.
    givenAFile("grandpa.js");

    // When you get the class name.
    String className = classFinder.getClassName();

    // Then ensure the name was found.
    assertEquals("Yo.grandpa", className);
  }

  public void testGetClassNameReverseConstructor() {
    // Given a constructor in the form of function MyClass() {}
    givenAFile("reverseConstructor.js");

    // When you get the class name.
    String className = classFinder.getClassName();

    // Then ensure the class name was found.
    assertEquals(className, "MyClass.ABC");
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

  public void testGetMethods() {
    // Given a file with functions.
    givenAFile("child.js");

    // When you get the functions.
    List<Function> methods = classFinder.getMethods();

    // Then ensure all the functions were found.
    assertEquals(4, methods.size());
    Function plainMethod = methods.get(0);
    assertEquals("plainMethod", plainMethod.getName());
    assertEquals("", plainMethod.getArguments());
    assertNull(plainMethod.getJsDoc());

    Function withArguments = methods.get(1);
    assertEquals("withArguments", withArguments.getName());
    assertEquals("one, two", withArguments.getArguments());
    assertNull(withArguments.getJsDoc());

    Function withManyLines = methods.get(2);
    assertEquals("withManyLines", withManyLines.getName());
    assertEquals("one, two,\n" +
        "              three", withManyLines.getArguments());
    assertNull(withManyLines.getJsDoc());

    Function withJsDoc = methods.get(3);
    assertEquals("withJsDoc", withJsDoc.getName());
    assertEquals("one, two, three", withJsDoc.getArguments());
    assertEquals("/**\n" +
        " *\n" +
        " * @param one\n" +
        " * @param two\n" +
        " * @param three\n" +
        " */", withJsDoc.getJsDoc());
  }

  public void testGeSortedMethods() {
    // Given a file with functions.
    givenAFile("child.js");

    // When you get the methods.
    List<Function> methods = classFinder.getSortedMethods();

    // Then ensure the methods are sorted.
    assertEquals("plainMethod", methods.get(0).getName());
    assertEquals("withArguments", methods.get(1).getName());
    assertEquals("withJsDoc", methods.get(2).getName());
    assertEquals("withManyLines", methods.get(3).getName());
  }

  private void givenAFile(String fileName) {
    prepareScenarioWithTestFile(fileName);
    classFinder = new ClassFinder(getDocument(virtualFile));
  }

  private Document getDocument(VirtualFile virtualFile) {
    return FileDocumentManager.getInstance().getDocument(virtualFile);
  }
}

