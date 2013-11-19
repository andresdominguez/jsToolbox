package com.karateca.jstoolbox.joiner;

/**
 * @author Andres Dominguez.
 */
class LineRange {
  private final int start;
  private final int end;

  LineRange(int start, int end) {
    this.start = start;
    this.end = end;
  }

  int getStart() {
    return start;
  }

  int getEnd() {
    return end;
  }
}
