package com.karateca.jstoolbox.assignmentstoobj;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andres Dominguez.
 */
class ToObjectConverter {
  private final String selectedCode;
  // Find the variable name in: "var foo =".
  public static final Pattern variableName = Pattern.compile("(\\s*var\\s+)?(\\w+)[\\s=]*");

  ToObjectConverter(String selectedCode) {
    this.selectedCode = selectedCode;
  }

  public String getObjectDeclaration() {
    // Does it match foo = {};?
    if (!firstLineMatchesObjDeclaration()) {
      return null;
    }

    // The the variable name.
    String variableName = getVarName();
    if (variableName == null) {
      return null;
    }

    StringBuilder sb = new StringBuilder();

    int braceIndex = selectedCode.indexOf("{");
    String substring = selectedCode.substring(0, braceIndex + 1);
    System.out.println(substring);

    while (true) {
      break;
    }

    int firstSemicolon = selectedCode.indexOf(";") + 1;
    String firstStmt = selectedCode.substring(0, firstSemicolon);


    sb.append(firstStmt);

    return sb.toString();
  }

  private boolean firstLineMatchesObjDeclaration() {
    Pattern pattern = Pattern.compile("(\\s*var\\s+)?\\w+\\s*=\\s*\\{\\s*\\};");
    String firstLine = StringUtils.substringBefore(selectedCode, "\n");
    Matcher matcher = pattern.matcher(firstLine);
    return matcher.find();
  }

  String getVarName() {
    int firstAssignment = selectedCode.indexOf("=");
    if (firstAssignment < 0) {
      return null;
    }

    String textUntilFirstAssignment = selectedCode.substring(0, firstAssignment);

    Matcher matcher = variableName.matcher(textUntilFirstAssignment);
    if (matcher.find()) {
      return matcher.group(2);
    }

    return null;
  }
}
