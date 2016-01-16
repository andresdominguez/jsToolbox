package com.karateca.jstoolbox.dashcamel;

import org.junit.Test;

import static org.junit.Assert.*;

public class CaseHelperTest {
  @Test
  public void shouldTransformToCamelCase() {
    assertEquals("oneTwoThree", CaseHelper.toCamelCase("one-two-three"));
    assertEquals("one", CaseHelper.toCamelCase("one"));
  }

  @Test
  public void shouldTransformEmptyString() {
    assertEquals("", CaseHelper.toCamelCase(""));
  }

  @Test
  public void shouldAcceptSpaces() {
    assertEquals("oneTwoThree", CaseHelper.toCamelCase("one two-three"));
    assertEquals("oneTwoThree", CaseHelper.toCamelCase("one  two-three"));
  }

  @Test
  public void shouldAcceptMultipleDashes() {
    assertEquals("oneTwoThreeee", CaseHelper.toCamelCase("one--two---threeee  "));
    assertEquals("oneTwoThreeee", CaseHelper.toCamelCase("---one--two---threeee---"));
  }

  @Test
  public void shouldCorrectUpperCase() {
    assertEquals("oneTwoThree", CaseHelper.toCamelCase("ONE-TWO-THREE"));
  }
}