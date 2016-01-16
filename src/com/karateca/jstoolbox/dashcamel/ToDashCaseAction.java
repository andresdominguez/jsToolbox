package com.karateca.jstoolbox.dashcamel;

public class ToDashCaseAction extends BaseCaseAction {

  @Override
  String changeCase(String selectedText) {
    return CaseHelper.toDashCase(selectedText);
  }
}
