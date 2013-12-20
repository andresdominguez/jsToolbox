package com.karateca.jstoolbox;

/**
 * @author Andres Dominguez.
 */
public class LineRange {
  private final int start;
  private final int end;

  public LineRange(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}
