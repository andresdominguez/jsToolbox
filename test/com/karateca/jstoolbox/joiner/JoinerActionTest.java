package com.karateca.jstoolbox.joiner;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;

/**
 * @author Andres Dominguez.
 */
public class JoinerActionTest extends LightCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return new File("testData").getPath();
  }

  public void testJoin() {
    myFixture.configureByFiles("multiLineStringBefore.js");

    myFixture.checkResultByFile("multiLineStringBefore.js", "multiLineStringAfter.js", false);
  }
}
