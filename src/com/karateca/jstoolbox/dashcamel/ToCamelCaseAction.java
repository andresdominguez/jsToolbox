package com.karateca.jstoolbox.dashcamel;

public class ToCamelCaseAction extends BaseCaseAction {

  String changeCase(String selectedText) {
    return CaseHelper.toCamelCase(selectedText);
  }
}
