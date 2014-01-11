package com.karateca.jstoolbox.assignmentstoobj;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andres Dominguez.
 */
class ToObjectConverter {
  private final String selectedCode;
  // Find the variable name in: "var foo =".
  public static final Pattern variableName = Pattern.compile("(\\s*var\\s+)?(\\w+)[\\s=]*");

  private final String indentation;

  ToObjectConverter(String selectedCode) {
    this.selectedCode = selectedCode;
    this.indentation = "  ";
  }

  public String getObjectDeclaration() {
    // Does it match foo = {};?
    if (!firstLineMatchesObjDeclaration()) {
      return null;
    }

    // Get the variable name from the start of the selection.
    String variableName = getVarName();
    if (variableName == null) {
      return null;
    }

    StringBuilder sb = new StringBuilder();

    int braceIndex = selectedCode.indexOf("{");
    String substring = selectedCode.substring(0, braceIndex + 1);
    sb.append(substring + "\n");

    List<String> declarations = getVariableDeclarations(variableName);

    String varStartWithDot = variableName + "\\.";

    for (String varDeclaration : declarations) {
      // Remove the "foo." from "foo.propertyName".
      String var = varDeclaration.replaceFirst(varStartWithDot, indentation);

      var = var.replaceFirst("\\s*=", ":");

      if (var.endsWith("\n")) {
        var = var.replaceAll(";\\n$", ",\n");
      } else {
        var = var.replaceAll(";$", ",");
      }

      sb.append(var);
    }

    sb.append("};");

    System.out.println(sb);

    return sb.toString();
  }

  /**
   * Get all the variable declarations from the selected code.
   *
   * @param variableName
   * @return A list of variables.
   */
  private List<String> getVariableDeclarations(String variableName) {
    List<Integer> varLocations = findVarLocations(variableName);
    List<String> varDeclarations = new ArrayList<String>();
    int count = varLocations.size();

    if (count > 1) {
      for (int i = 1; i < count - 1; i++) {
        Integer index = varLocations.get(i);
        Integer nextIndex = varLocations.get(i + 1);

        varDeclarations.add(selectedCode.substring(index, nextIndex));
      }
    }

    return varDeclarations;
  }

  private List<Integer> findVarLocations(String variableName) {
    List<Integer> locations = new ArrayList<Integer>();

    int from = 0;

    while (true) {
      int index = this.selectedCode.indexOf(variableName, from);
      if (index < 0) {
        locations.add(selectedCode.length());
        return locations;
      }

      locations.add(index);
      from = index + 1;
    }
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
