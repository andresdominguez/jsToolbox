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

    // Get the variable name.
    int start = objectString.indexOf("{") + 1;

    sb.append(this.objectString.substring(0, start));
    sb.append("}\n");

    return sb.toString();
  }

  public String getVariableName() {
    // Get the variable name by finding all the substing until the first '{'.
    int braceIndex = objectString.indexOf("{");
    String substring = objectString.substring(0, braceIndex);

    Pattern pattern = Pattern.compile("(\\s*\\w*\\s*)(\\w+)(\\s*=\\s*)");
    Matcher matcher = pattern.matcher(substring);
    if (matcher.find()) {
      return matcher.group(2);
    }
    return null;
  }

  private int getNextVar(int start) {
    int runner = start;

    int i = objectString.indexOf(",", start);

    return i;
  }
}
