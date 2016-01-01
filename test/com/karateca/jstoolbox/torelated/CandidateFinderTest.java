package com.karateca.jstoolbox.torelated;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CandidateFinderTest {

  @Test
  public void shouldFindCandidates() {
    List<String> fromSuffixes = Arrays.asList(".js", "-controller.js");
    List<String> toSuffixes = Arrays.asList("_spec.js", "-spec.js");

    List<String> candidates = CandidateFinder.suggestDestinationFiles(
        "foo-controller.js", fromSuffixes, toSuffixes);

    assertEquals(4, candidates.size());
    assertTrue(candidates.contains("foo-controller_spec.js"));
    assertTrue(candidates.contains("foo-controller-spec.js"));
    assertTrue(candidates.contains("foo-spec.js"));
    assertTrue(candidates.contains("foo_spec.js"));
  }
}