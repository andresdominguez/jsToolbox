package com.karateca.jstoolbox.objtoassignments;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsTransformer {
  final String objectString;
  public static final Pattern NAME_VALUE_PATTERN = Pattern.compile("['\"]?(\\w+)['\"]?\\s*:(.+)");

  public ObjectToAssignmentsTransformer(String objectString) {
    this.objectString = objectString;
  }


  public String getAssignments() {
    StringBuilder sb = new StringBuilder();

    String variableName = getVariableName();
    if (variableName == null) {
      return "";
    }

    // Start building the string with: "var varName = {".
    int start = objectString.indexOf("{") + 1;
    sb.append(objectString.substring(0, start)).append("};\n");

    // Transform each variable name.
    int currentOffset = start;
    List<Integer> variableLocations = findVariableLocations();
    String assignment = "";

    for (Integer location : variableLocations) {
      assignment = getVarNameAndAssignmentValue(currentOffset, location);

      sb.append(variableName);
      sb.append(assignment);

      currentOffset = location;
    }

    return sb.toString();
  }

  private String getVarNameAndAssignmentValue(int currentOffset, Integer location) {
    String assignment;
    String assignmentStmt = objectString.substring(currentOffset, location);

    assignment = "";
    Matcher matcher = NAME_VALUE_PATTERN.matcher(assignmentStmt.trim());
    if (matcher.find()) {
      String name = matcher.group(1);
      String value = matcher.group(2).trim();

      if (value.endsWith(",")) {
        value = value.substring(0, value.length() - 1);
      }

      assignment = String.format(".%s = %s;\n", name, value);
    }
    return assignment;
  }

  public String getVariableName() {
    String substring = objectString;

    Pattern pattern = Pattern.compile("(\\s*\\w*\\s*)(\\w+)(\\s*=\\s*)");
    Matcher matcher = pattern.matcher(substring);
    if (matcher.find()) {
      return matcher.group(2);
    }
    return null;
  }

  private List<Integer> findVariableLocations() {
    Pattern pattern = Pattern.compile("['\"]?\\w+['\"]?\\s*:.*");

    Matcher matcher = pattern.matcher(objectString);

    List<Integer> locations = new ArrayList<Integer>();


    while (matcher.find()) {
      locations.add(matcher.start());
    }
    locations.add(objectString.length() - 1);

    return locations;
  }
}
