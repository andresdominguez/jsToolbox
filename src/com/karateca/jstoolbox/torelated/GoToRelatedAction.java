package com.karateca.jstoolbox.torelated;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.karateca.jstoolbox.MyAction;
import com.karateca.jstoolbox.config.JsToolboxSettings;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.karateca.jstoolbox.torelated.CandidateFinder.suggestDestinationFiles;

/**
 * foo-controller.js to vie
 * <p>
 * foo-controller * view suffixes
 * foo * view suffixes
 */

abstract class GoToRelatedAction extends MyAction {

  List<String> fileSuffixList;
  List<String> viewSuffixList;
  List<String> testSuffixList;

  @Override
  public void actionPerformed(AnActionEvent event) {
    // Disable if dependencies are not met.
    if (!canEnableAction(event)) {
      return;
    }

    readConfig();
    performSwitch(event, getCurrentFileName(event));
  }

  void performSwitch(AnActionEvent event, String fileName) {
    List<String> fromSuffixes = suffixesForType(getFileType(fileName));
    List<String> toSuffixes = suffixesForType(getNavigateTo(fileName));

    goToFiles(event, suggestDestinationFiles(fileName, fromSuffixes, toSuffixes));
  }

  List<String> suffixesForType(FileType fileType) {
    if (fileType == FileType.FILE) {
      return fileSuffixList;
    }

    if (fileType == FileType.TEST) {
      return testSuffixList;
    }

    return viewSuffixList;
  }

  abstract FileType getNavigateTo(String fileName);

  void readConfig() {
    JsToolboxSettings settings = new JsToolboxSettings();

    fileSuffixList = Arrays.asList(settings.getFileSuffix().split(","));
    viewSuffixList = Arrays.asList(settings.getViewSuffix().split(","));
    testSuffixList = Arrays.asList(settings.getTestSuffix().split(","));
  }

  boolean isViewFile(String fileName) {
    return endsWithAnyOf(fileName, viewSuffixList);
  }

  boolean isTestFile(String fileName) {
    return endsWithAnyOf(fileName, testSuffixList);
  }

  FileType getFileType(String fileName) {
    if (isViewFile(fileName)) {
      return FileType.VIEW;
    }

    if (isTestFile(fileName)) {
      return FileType.TEST;
    }

    return FileType.FILE;
  }

  private boolean endsWithAnyOf(String fileName, List<String> destinationSuffix) {
    for (String suffix : destinationSuffix) {
      if (fileName.endsWith(suffix)) {
        return true;
      }
    }
    return false;
  }

  void goToFiles(AnActionEvent e, List<String> destinationFiles) {
    String fileName = getCurrentFileName(e);
    String filePath = getCurrentFilePath(e);

    for (String goToFileName : destinationFiles) {
      List<PsiFile> files = findFilesInProjectWithName(goToFileName, e.getProject());

      if (files.size() == 1) {
        files.get(0).navigate(true);
      } else if (files.size() > 1) {
        // There is more that one match. Look for the closest match.
        Map<String, PsiFile> filesByPath = groupFilesByPath(files);
        String targetPath = filePath.replace(fileName, goToFileName);
        String longest = LongestSuffix.find(filesByPath.keySet(), targetPath);
        if (longest != null) {
          filesByPath.get(longest).navigate(true);
        }
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

  List<PsiFile> findFilesInProjectWithName(String fileName, Project project) {
    FindRelatedFileIterator iterator =
        new FindRelatedFileIterator(fileName, PsiManager.getInstance(project));

    ProjectRootManager.getInstance(project).getFileIndex().iterateContent(iterator);

    return iterator.getFiles();
  }
}
