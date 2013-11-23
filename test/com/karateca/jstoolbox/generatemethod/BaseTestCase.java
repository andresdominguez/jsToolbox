package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import junit.framework.Assert;

import java.io.File;

/**
 * @author Andres Dominguez.
 */
public class BaseTestCase extends LightCodeInsightFixtureTestCase {

  protected VirtualFile virtualFile;
  protected PsiFile psiFile;

  @Override
  protected String getTestDataPath() {
    return new File("testData").getPath();
  }

  public void testDummyTest() {
    // Created this test to get rid of the warning.
    Assert.assertEquals(1, 1);
  }

  protected void prepareScenarioWithTestFile(String fileName) {
    psiFile = myFixture.configureByFile(fileName);
    virtualFile = psiFile.getVirtualFile();
  }
}
