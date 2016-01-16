package com.karateca.jstoolbox.dashcamel;

import org.junit.Test;

import static org.junit.Assert.*;

public class CaseHelperTest {
  @Test
  public void shouldTransformToCamelCase() {
    assertEquals("oneTwoThree", CaseHelper.toCamelCase("one-two-three"));
  }

  @Test
  public void shouldTransformEmptyString() {
    assertEquals("", CaseHelper.toCamelCase(""));
  }

  @Test
  public void shouldAcceptSpaces() {
    assertEquals("oneTwoThree", CaseHelper.toCamelCase("one two-three"));
  }
}