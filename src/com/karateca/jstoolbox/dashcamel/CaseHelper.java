package com.karateca.jstoolbox.dashcamel;

public class CaseHelper {
  public static String toCamelCase(String dashCase) {
    // Split on dash and space
    String[] parts = dashCase.toLowerCase().split("[- ]+");
    StringBuilder builder = new StringBuilder();

    // Make the first character upper case
    for (String part : parts) {
      if (part.length() > 0) {
        builder.append(part.substring(0, 1).toUpperCase());
        builder.append(part.substring(1));
      }
    }

    // First char is lower case
    if (builder.length() > 0) {
      String firstChar = builder.substring(0, 1).toLowerCase();
      builder.replace(0, 1, firstChar);
    }

    return builder.toString();
  }
}
