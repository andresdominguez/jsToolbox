package com.karateca.jstoolbox.dashcamel;

import java.util.Arrays;
import java.util.List;

public class CaseHelper {
  public static String toCamelCase(String dashCase) {
    // Split on dash and space
    List<String> parts = Arrays.asList(dashCase.split("[- ]"));
    StringBuilder builder = new StringBuilder(parts.get(0));

    for (String part : parts.subList(1, parts.size())) {
      builder.append(part.substring(0, 1).toUpperCase());
      builder.append(part.substring(1));
    }

    return builder.toString();
  }
}
