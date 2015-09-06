package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class GoToRelatedAction extends MyAction {

  abstract List<String> getDestinationSuffixList();

  List<String> fileSuffixList;
  List<String> viewSuffixList;
  List<String> testSuffixList;

  @Override
  public void actionPerformed(AnActionEvent e) {
    // Disable if dependencies are not met.
    if (!canEnableAction(e)) {
      return;
    }

    readConfig();

    String fileName = getCurrentFileName(e);

    // Is it at destination? Go back to file.
    if (isInDestinationFile(fileName)) {
      goToFiles(e, getDestinationMatch(fileName), fileSuffixList);
    } else if (isInCodeFile(fileName)) {
      // If I'm in file then go to destination.
      goToFiles(e, getCodeFileMatch(fileName), getDestinationSuffixList());
    } else if (isViewFile(fileName)) {
      // Go from view to test.
      goToFiles(e, getViewSuffixMatch(fileName), testSuffixList);
    } else if (isTestFile(fileName)) {
      // Go from test to view.
      goToFiles(e, getTestSuffixMatch(fileName), viewSuffixList);
    }
  }

  void readConfig() {
    JsToolboxSettings settings = new JsToolboxSettings();

    fileSuffixList = Arrays.asList(settings.getFileSuffix().split(","));
    viewSuffixList = Arrays.asList(settings.getViewSuffix().split(","));
    testSuffixList = Arrays.asList(settings.getTestSuffix().split(","));
  }

  String getDestinationMatch(String fileName) {
    return findMatch(fileName, getDestinationSuffixList());
  }

  String getCodeFileMatch(String fileName) {
    return findMatch(fileName, fileSuffixList);
  }

  String getTestSuffixMatch(String fileName) {
    return findMatch(fileName, testSuffixList);
  }

  String getViewSuffixMatch(String fileName) {
    return findMatch(fileName, viewSuffixList);
  }

  boolean isInCodeFile(String fileName) {
    return !isTestFile(fileName) && !isViewFile(fileName);
  }

  boolean isViewFile(String fileName) {
    return endsWithAnyOf(fileName, viewSuffixList);
  }

  boolean isTestFile(String fileName) {
    return endsWithAnyOf(fileName, testSuffixList);
  }

  boolean isInDestinationFile(String fileName) {
    return endsWithAnyOf(fileName, getDestinationSuffixList());
  }

  private String findMatch(String fileName, List<String> suffixList) {
    for (String suffix : suffixList) {
      if (fileName.endsWith(suffix)) {
        return suffix;
      }
    }
    return null;
  }

  private boolean endsWithAnyOf(String fileName, List<String> destinationSuffix) {
    for (String suffix : destinationSuffix) {
      if (fileName.endsWith(suffix)) {
        return true;
      }
    }
    return false;
  }

  void goToFiles(AnActionEvent e, String fromSuffix, List<String> toSuffixes) {
    String fileName = getCurrentFileName(e);
    String filePath = getCurrentFilePath(e);

    for (String suffix : toSuffixes) {
      String goToFileName = fileName.replace(fromSuffix, suffix);

      List<PsiFile> files = openFileInEditor(goToFileName, e.getProject());
      if (files.size() == 0) {
        return;
      }

      if (files.size() == 1) {
        files.get(0).navigate(true);
      }

      // There is more that one match. Look for the closest match.
      Map<String, PsiFile> filesByPath = groupFilesByPath(files);
      String targetPath = filePath.replace(fileName, goToFileName);
      String longest = LongestSuffix.find(filesByPath.keySet(), targetPath);
      if (longest != null) {
        filesByPath.get(longest).navigate(true);
      }
    }
  }

  @NotNull
  private Map<String, PsiFile> groupFilesByPath(List<PsiFile> files) {
    Map<String, PsiFile> filesByPath = new HashMap<>();
    for (PsiFile file : files) {
      String path = file.getVirtualFile().getCanonicalPath();
      filesByPath.put(path, file);
    }
    return filesByPath;
  }

  List<PsiFile> openFileInEditor(String fileName, Project project) {
    FindRelatedFileIterator iterator =
        new FindRelatedFileIterator(fileName, PsiManager.getInstance(project));

    ProjectRootManager.getInstance(project).getFileIndex().iterateContent(iterator);

    return iterator.getFiles();
  }
}
