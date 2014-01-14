package com.karateca.jstoolbox.assignmentstoobj;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;

/**
 * @author Andres Dominguez.
 */
public class AssignmentsToObjectActionTest extends LightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return new File("testData/assignmentsToObject").getPath();
  }

  public void testTransformToObject() {
    runActionOnFile("assignmentsToObjectBefore.js");

    myFixture.checkResultByFile(
        "assignmentsToObjectBefore.js",
        "assignmentsToObjectAfter.js",
        true);
  }

  public void testTransformPartialSelection() {
    runActionOnFile("assignmentsToObjectPartialSelBefore.js");

    myFixture.checkResultByFile(
        "assignmentsToObjectPartialSelBefore.js",
        "assignmentsToObjectPartialSelAfter.js",
        true
    );
  }

  private void runActionOnFile(String fileName) {
    myFixture.configureByFiles(fileName);

    myFixture.performEditorAction("com.karateca.jstoolbox.assignmentstoobj.AssignmentsToObjectAction");
  }
}
