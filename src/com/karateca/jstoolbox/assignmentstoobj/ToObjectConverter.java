package com.karateca.jstoolbox.assignmentstoobj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andres Dominguez.
 */
class ToObjectConverter {
  private final String selectedCode;
  public static final Pattern variableName = Pattern.compile("(\\s*var\\s+)(\\w+)[\\s=]*");

  ToObjectConverter(String selectedCode) {
    this.selectedCode = selectedCode;
  }

  public String getObjectDeclaration() {
    String variableName = getVarName();

    System.out.println(variableName);

    return "";
  }

  String getVarName() {
    int firstAssignment = selectedCode.indexOf("=");
    if (firstAssignment < 0) {
      return null;
    }

    String textUntilFirstAssignment = selectedCode.substring(0, firstAssignment);

    System.out.println(textUntilFirstAssignment);

    Matcher matcher = variableName.matcher(textUntilFirstAssignment);
    if (matcher.find()) {
      return matcher.group(2);
    }

    return null;
  }
}
