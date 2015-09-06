package com.karateca.jstoolbox.torelated;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LongestSuffixTest {
  @Test
  public void canFindLongest() {
    List<String> paths = Arrays.asList(
        "/users/foo/dev/test/project/one/two/three.js",
        "/users/foo/dev/test/project/one/two/four/three.js",
        "/users/foo/dev/test/project/one/two/four/five/three.js"
    );

    String path = "/users/foo/dev/project/one/two/four/five/three.js";
    String match =  LongestSuffix.find(paths, path);

    assertEquals("/users/foo/dev/test/project/one/two/four/five/three.js", match);
  }
}