package com.karateca.jstoolbox.dashcamel;

import org.junit.Test;

import static org.junit.Assert.*;

public class CaseHelperTest {
  @Test
  public void shouldTransformToCamelCase() {
    assertEquals("oneTwoThree", CaseHelper.toggleCase("one-two-three"));
    assertEquals("one", CaseHelper.toggleCase("one"));
    assertEquals("o", CaseHelper.toggleCase("o"));
  }

  @Test
  public void shouldTransformEmptyString() {
    assertEquals("", CaseHelper.toggleCase(""));
  }

  @Test
  public void shouldAcceptSpaces() {
    assertEquals("oneTwoThree", CaseHelper.toggleCase("one two-three"));
    assertEquals("oneTwoThree", CaseHelper.toggleCase("one  two-three"));
    assertEquals("x", CaseHelper.toggleCase("          x        -- - - -  "));
  }

  @Test
  public void shouldAcceptMultipleDashes() {
    assertEquals("oneTwoThreeee", CaseHelper.toggleCase("one--two---threeee  "));
    assertEquals("oneTwoThreeee", CaseHelper.toggleCase("---one--two---threeee---"));
  }

  @Test
  public void shouldCorrectUpperCase() {
    assertEquals("oneTwoThree", CaseHelper.toggleCase("ONE-TWO-THREE"));
  }

  @Test
  public void shouldTransformCamelToDash() {
    assertEquals("one-two", CaseHelper.toggleCase("oneTwo"));
    assertEquals("one-two", CaseHelper.toggleCase("OneTwo"));
  }
}