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
    myFixture.configureByFiles("assignmentsToObjectBefore.js");

    myFixture.performEditorAction("com.karateca.jstoolbox.assignmentstoobj.AssignmentsToObjectAction");

    myFixture.checkResultByFile(
        "assignmentsToObjectBefore.js",
        "assignmentsToObjectAfter.js", true);
  }
}
