package com.karateca.jstoolbox.generatemethod;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Andres Dominguez.
 */
public class FunctionTest {

  @Test
  public void testGetExtendedJsDocNullDoc() throws Exception {
    // Given a function without js doc.
    Function function = new Function("", "", null);

    // When you the the extended js doc.
    // Then ensure it contains the default jsdoc.
    assertEquals("/**\n" +
        " * @override\n" +
        " */", function.getExtendedJsDoc());
  }

  @Test
  public void testGetExtendedJsDocWithJsDoc() {
    String jsDoc = "/**\n" +
        " * Do the.\n" +
        " * @param {string} a The a.\n" +
        " * @param {string} b The a.\n" +
        " * @param {string} c The a.\n" +
        " */\n";

    Function function = new Function(null, null, jsDoc);

    assertEquals("/**\n" +
        " * Do the.\n" +
        " * @override\n" +
        " * @param {string} a The a.\n" +
        " * @param {string} b The a.\n" +
        " * @param {string} c The a.\n" +
        " */\n", function.getExtendedJsDoc());
  }

  @Test
  public void testGetExtendedJsDocWithoutParams() {
    String jsDoc = "/**\n" +
        " * Hey.\n" +
        " */\n";

    Function function = new Function(null, null, jsDoc);

    assertEquals("/**\n" +
        " * Hey.\n" +
        " * @override\n" +
        " */\n", function.getExtendedJsDoc());
  }

  @Test
  public void testShouldNotAddExtraOverride() {
    String jsDoc = "/**\n" +
        " * @override\n" +
        " */\n";

    Function function = new Function(null, null, jsDoc);


    assertEquals("/**\n" +
        " * @override\n" +
        " */\n", function.getExtendedJsDoc());
  }
}
