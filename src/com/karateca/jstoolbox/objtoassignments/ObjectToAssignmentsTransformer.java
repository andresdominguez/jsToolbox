package com.karateca.jstoolbox.objtoassignments;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsTransformer {
  final String objectString;
  // Look for: "name: value".
  public static final Pattern NAME_VALUE_PATTERN = Pattern.compile("['\"]?(\\w+)['\"]?\\s*:(.+)");
  public static final Pattern BRACE_OR_FUNCTION = Pattern.compile("\\s*(\\{|[\\w\\.]+\\s*\\()");

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
    List<Integer> variableLocations = findVariableLocations();

    int currentOffset = start;

    System.out.println(variableLocations);

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
    String assignmentStmt = objectString.substring(currentOffset, location);

    Matcher matcher = NAME_VALUE_PATTERN.matcher(assignmentStmt.trim());
    if (matcher.find()) {
      String name = matcher.group(1);
      String value = matcher.group(2).trim();

      if (value.endsWith(",")) {
        value = value.substring(0, value.length() - 1);
      }

      return String.format(".%s = %s;\n", name, value);
    }
    return null;
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

    int prevMatch = 0;
    int searchFrom = 0;
    while (matcher.find(searchFrom)) {
      int matchIndex = matcher.start();

      if (currentMatchIsNotLiteral(prevMatch, matchIndex)) {
        // Find the closing index of the closing brace.
        int closingBraceIndex = findClosingBrace(prevMatch);
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

  private int findClosingBrace(int fromIndex) {
    Stack<Character> stack = new Stack<Character>();

    int length = objectString.length();
    for (int i = fromIndex; i < length; i++) {
      char c = objectString.charAt(i);
      if (c == '{') {
        stack.push(c);
      } else if (c == '}') {
        if (stack.isEmpty()) {
          return fromIndex;
        }

        stack.pop();
        if (stack.isEmpty()) {
          return i;
        }
      }
    }
    return 0;  //To change body of created methods use File | Settings | File Templates.
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
