package com.karateca.jstoolbox.joiner;

import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;

/**
 * @author Andres Dominguez.
 */
public class JoinerActionTest extends LightCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return new File("testData/joiner").getPath();
  }

  public void testJoinStringSingleQuotes() {
    myFixture.configureByFiles("multiLineStringBefore.js");

    myFixture.performEditorAction("com.karateca.jstoolbox.joiner.JoinerAction");

    myFixture.checkResultByFile(
        "multiLineStringBefore.js",
        "multiLineStringAfter.js", false);
  }

  public void testJoinStringDoubleQuotes() {
    myFixture.configureByFiles("multiLineStringDoubleQuotesBefore.js");

    myFixture.performEditorAction("com.karateca.jstoolbox.joiner.JoinerAction");

    myFixture.checkResultByFile(
        "multiLineStringDoubleQuotesBefore.js",
        "multiLineStringDoubleQuotesAfter.js",
        false);
  }
}
