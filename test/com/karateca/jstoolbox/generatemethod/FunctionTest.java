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
    // When you the the extended js doc.
    String extendedJsDoc = getExtendedJs(null);

    // Then ensure it contains the default jsdoc.
    assertEquals("/**\n" +
        " * @override\n" +
        " */", extendedJsDoc);
  }

  @Test
  public void testGetExtendedJsDocWithJsDoc() {
    String jsDoc = "/**\n" +
        " * Do the.\n" +
        " * @param {string} a The a.\n" +
        " * @param {string} b The a.\n" +
        " * @param {string} c The a.\n" +
        " */\n";

    String extendedJsDoc = getExtendedJs(jsDoc);

    assertEquals("/**\n" +
        " * Do the.\n" +
        " * @override\n" +
        " * @param {string} a The a.\n" +
        " * @param {string} b The a.\n" +
        " * @param {string} c The a.\n" +
        " */\n", extendedJsDoc);
  }

  @Test
  public void testGetExtendedJsDocWithoutParams() {
    String jsDoc = "/**\n" +
        " * Hey.\n" +
        " */\n";

    String extendedJsDoc = getExtendedJs(jsDoc);

    assertEquals("/**\n" +
        " * Hey.\n" +
        " * @override\n" +
        " */\n", extendedJsDoc);
  }

  @Test
  public void testShouldNotAddExtraOverride() {
    String jsDoc = "/**\n" +
        " * @override\n" +
        " */\n";

    String extendedJsDoc = getExtendedJs(jsDoc);

    assertEquals("/**\n" +
        " * @override\n" +
        " */\n", extendedJsDoc);
  }

  private String getExtendedJs(String jsDoc) {
    Function function = new FunctionBuilder()
        .setName(null)
        .setArguments(null)
        .setJsDoc(jsDoc)
        .setClassName(null)
        .createFunction();

    return function.getExtendedJsDoc();
  }
}
