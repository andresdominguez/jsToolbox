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
    myFixture.configureByFiles("objectToAssignmentsBefore.js");

    myFixture.performEditorAction("com.karateca.jstoolbox.objtoassignments.ObjectToAssignmentsAction");

    myFixture.checkResultByFile(
        "objectToAssignmentsBefore.js",
        "objectToAssignmentsAfter.js", true);
  }
}
