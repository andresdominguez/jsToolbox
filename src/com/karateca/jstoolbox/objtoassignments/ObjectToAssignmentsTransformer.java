package com.karateca.jstoolbox.objtoassignments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andres Dominguez.
 */
public class ObjectToAssignmentsTransformer {
  final String objectString;

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
    sb.append(this.objectString.substring(0, start)).append("}\n");

    // Transform each variable name.

    return sb.toString();
  }

  public String getVariableName() {
    // Get the variable name by finding all the substring until the first '{'.
    int braceIndex = objectString.indexOf("{");
    String substring = objectString.substring(0, braceIndex);

    Pattern pattern = Pattern.compile("(\\s*\\w*\\s*)(\\w+)(\\s*=\\s*)");
    Matcher matcher = pattern.matcher(substring);
    if (matcher.find()) {
      return matcher.group(2);
    }
    return null;
  }
}
