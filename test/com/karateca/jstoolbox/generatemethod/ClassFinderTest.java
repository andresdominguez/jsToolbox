package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * @author Andres Dominguez.
 */
public class ClassFinderTest extends BaseTestCase {

  public void testGetClassNameClassFound() {
    // Given a file with a constructor.
    givenAFile("child.js");

    // When you get the class name.
    ClassFinder classFinder = new ClassFinder(getDocument(virtualFile));
    String className = classFinder.getClassName();

    // Then ensure the class name was found.
    assertEquals("Yo.child", className);
  }

  public void testGetClassNameClassNotFound() {
    // Given a file without constructor annotation.
    givenAFile("noConstructor.js");

    // When you get the class name.
    ClassFinder classFinder = new ClassFinder(getDocument(virtualFile));
    String className = classFinder.getClassName();

    // Then ensure the class name was not found.
    assertNull(className);
  }

  private void givenAFile(String fileName) {
    prepareScenarioWithTestFile(fileName);
  }

  private Document getDocument(VirtualFile virtualFile) {
    return FileDocumentManager.getInstance().getDocument(virtualFile);
  }
}

