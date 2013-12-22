package com.karateca.jstoolbox.objtoassignments;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsTransformer {
  private final String objectString;
  // Look for: "name: value".
  private static final Pattern VARIABLE_NAME = Pattern.compile("['\"]?(\\w+)['\"]?");
  // Determine if there is an opening brace or function in the current line.
  private static final Pattern BRACE_OR_FUNCTION = Pattern.compile("\\s*(\\{|[\\w\\.]+\\s*\\()");
  // Look for: "var varName =".
  public static final Pattern FIRST_LINE_VAR_NAME = Pattern.compile("(\\s*\\w*\\s*)(\\w+)(\\s*=\\s*)");

  public ObjectToAssignmentsTransformer(String objectString) {
    this.objectString = objectString;
  }

  public String toAssignments() {
    String variableName = getVariableName();
    if (variableName == null) {
      // TODO: return null and handle null in the action.
      return "";
    }

    StringBuilder sb = new StringBuilder();

    // Start building the string with: "var varName = {".
    int start = objectString.indexOf("{") + 1;
    sb.append(objectString.substring(0, start)).append("};\n");

    // Transform each variable name.
    List<Integer> variableLocations = findVariableLocations();

    int currentOffset = start;

    for (Integer varLocation : variableLocations) {
      String assignment = getVarNameAndAssignmentValue(currentOffset, varLocation);
      if (assignment != null) {
        // foo
        sb.append(variableName);
        // .name = value
        sb.append(assignment);

        currentOffset = varLocation;
      }
    }

    return sb.toString();
  }

  private String getVarNameAndAssignmentValue(int currentOffset, Integer location) {
    String assignmentStmt = objectString.substring(currentOffset, location).trim();

    int colonIndex = assignmentStmt.indexOf(":");
    if (colonIndex < 0) {
      return null;
    }

    Matcher matcher = VARIABLE_NAME.matcher(assignmentStmt.substring(0, colonIndex));
    if (!matcher.find()) {
      return null;
    }

    String name = matcher.group(1);
    String value = assignmentStmt.substring(colonIndex + 1).trim();

    if (value.endsWith(",")) {
      value = value.substring(0, value.length() - 1);
    }

    return String.format(".%s = %s;\n", name, value);
  }

  private String getVariableName() {
    Matcher matcher = FIRST_LINE_VAR_NAME.matcher(objectString);
    if (matcher.find()) {
      return matcher.group(2);
    }
    return null;
  }

  private List<Integer> findVariableLocations() {
    Pattern pattern = Pattern.compile("['\"]?\\w+['\"]?\\s*:.*");

    Matcher matcher = pattern.matcher(objectString);

    List<Integer> locations = new ArrayList<Integer>();

    int prevMatch = 0;
    int searchFrom = 0;
    while (matcher.find(searchFrom)) {
      int matchIndex = matcher.start();

      if (currentMatchIsNotLiteral(prevMatch, matchIndex)) {
        // Find the closing index of the closing brace.
        int closingBraceIndex = BraceMatcher.getClosingBraceIndex(objectString, prevMatch);
        locations.add(closingBraceIndex);
        searchFrom = closingBraceIndex;
        prevMatch = closingBraceIndex;
      } else {
        locations.add(matchIndex);
        searchFrom = matcher.end();
        prevMatch = matchIndex;
      }
    }
    locations.add(objectString.length() - 1);

    return locations;
  }

  private boolean currentMatchIsNotLiteral(int fromIndex, int toIndex) {
    // Ignore first instance.
    if (fromIndex == 0) {
      return false;
    }

    String expression = objectString.substring(fromIndex, toIndex);
    expression = expression.substring(expression.indexOf(":") + 1);

    return BRACE_OR_FUNCTION.matcher(expression).find();
  }
}
