package com.karateca.jstoolbox.torelated;


import java.util.ArrayList;
import java.util.List;

public class CandidateFinder {

  static List<String> suggestDestinationFiles(
      String fileName, List<String> fromSuffixes, List<String> toSuffixes) {
    List<String> candidates = new ArrayList<>();

    for (String fromSuffix : fromSuffixes) {
      if (fileName.endsWith(fromSuffix)) {
        String withoutSuffix = fileName.substring(0, fileName.length() - fromSuffix.length());
        for (String toSuffix : toSuffixes) {
          candidates.add(withoutSuffix + toSuffix);
        }
      }
    }

    return candidates;
  }
}
