package com.karateca.jstoolbox.objtoassignments;

import java.util.Stack;

/**
 * @author Andres Dominguez.
 */
class BraceMatcher {
  public static int getClosingBraceIndex(String codeText, int fromIndex) {
    Stack<Character> stack = new Stack<Character>();

    int length = codeText.length();
    for (int i = fromIndex; i < length; i++) {
      char c = codeText.charAt(i);
      if (c == '{') {
        stack.push(c);
      } else if (c == '}') {
        if (stack.isEmpty()) {
          return -1;
        }

        stack.pop();
        if (stack.isEmpty()) {
          return i + 1;
        }
      }
    }
    // Didn't find anything.
    return -1;
  }
}
