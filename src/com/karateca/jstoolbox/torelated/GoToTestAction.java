package com.karateca.jstoolbox.torelated;

public class GoToTestAction extends GoToRelatedAction {

  @Override
  FileType getNavigateTo(String fileName) {
    FileType from = getFileType(fileName);

    if (from == FileType.TEST) {
      return FileType.FILE;
    }
    return FileType.TEST;
  }
}
