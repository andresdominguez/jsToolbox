package com.karateca.jstoolbox.torelated;

import org.hamcrest.Matchers;
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

    assertThat(candidates, Matchers.containsInAnyOrder(
        "foo-controller_spec.js",
        "foo-controller-spec.js",
        "foo-spec.js",
        "foo_spec.js"));
  }
}