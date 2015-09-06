package com.karateca.jstoolbox.torelated;

public class GoToViewAction extends GoToRelatedAction {

  @Override
  FileType getNavigateTo(String fileName) {
    FileType from = getFileType(fileName);

    if (from == FileType.VIEW) {
      return FileType.FILE;
    }
    return FileType.VIEW;
  }
}
