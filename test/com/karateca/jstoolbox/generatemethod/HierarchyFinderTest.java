package com.karateca.jstoolbox.generatemethod;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andres Dominguez.
 */
public class HierarchyFinderTest extends LightCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return new File("testData").getPath();
  }

  public void testFindParents() throws Exception {
    List<VirtualFile> virtualFiles = prepareScenarioWithTestFiles("child.js", "parent.js", "grandpa.js");

    HierarchyFinder hierarchyFinder = new HierarchyFinder(getProject(), getDocument(virtualFiles.get(0))) {
      @Override
      protected Document getDocument(VirtualFile virtualFile) {
        try {
          String s = new String(virtualFile.contentsToByteArray());
          return new DocumentImpl(s);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    };

    HierarchyResults parents = hierarchyFinder.findParents();

    List<Document> hierarchy = parents.getHierarchy();
    assertEquals(3, hierarchy.size());
  }

  private List<VirtualFile> prepareScenarioWithTestFiles(String... fileNames) throws IOException {
    List<VirtualFile> virtualFiles = new ArrayList<VirtualFile>();

    for (String fileName : fileNames) {
      VirtualFile virtualFile = myFixture.copyFileToProject(fileName);
      virtualFiles.add(virtualFile);
    }

    return virtualFiles;
  }

  private Document getDocument(VirtualFile virtualFile) throws IOException {
    String s = new String(virtualFile.contentsToByteArray());
    return new DocumentImpl(s);
  }
}
