package com.karateca.jstoolbox.objtoassignments;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsActionTest extends LightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return new File("testData/objectToAssignments").getPath();
  }

  public void testTransformObjectIntoAssignments() {
    runActionWithFile("objectToAssignmentsBefore.js");

    myFixture.checkResultByFile(
        "objectToAssignmentsBefore.js",
        "objectToAssignmentsAfter.js",
        true);
  }

  public void testTransformWithSelection() {
    runActionWithFile("objectToAssignmentsSelectionBefore.js");

    myFixture.checkResultByFile(
        "objectToAssignmentsSelectionBefore.js",
        "objectToAssignmentsSelectionAfter.js",
        true
    );
  }

  private void runActionWithFile(String fileName) {
    myFixture.configureByFiles(fileName);

    myFixture.performEditorAction("com.karateca.jstoolbox.objtoassignments.ObjectToAssignmentsAction");
  }
}
