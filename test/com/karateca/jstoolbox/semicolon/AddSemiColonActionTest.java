package com.karateca.jstoolbox.semicolon;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;

public class AddSemiColonActionTest extends LightCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return new File("testData/semicolon").getPath();
  }

  public void testAddSemicolon() {
    myFixture.configureByFiles("semicolonBefore.js");

    myFixture.performEditorAction("com.karateca.jstoolbox.semicolon.AddSemiColonAction");

    myFixture.checkResultByFile(
        "semicolonBefore.js",
        "semicolonAfter.js", false);
  }
}
