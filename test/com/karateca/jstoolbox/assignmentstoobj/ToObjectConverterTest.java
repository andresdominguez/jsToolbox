package com.karateca.jstoolbox.assignmentstoobj;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Andres Dominguez.
 */
public class ToObjectConverterTest {

  private ToObjectConverter converter;

  @Before
  public void createConverter() {
    String codeText = "var theThing = {};\n" +
        "theThing.first = 'Jackie';\n" +
        "theThing.last = 'Chan';\n" +
        "theThing.phone = {\n" +
        "  areaCode: 212,\n" +
        "  phone: '123-4567'\n" +
        "};\n";
    converter = new ToObjectConverter(codeText);
  }

  @Test
  public void testFindVariableName() {
    String varName = converter.getVarName();
    assertEquals(varName, "theThing");
  }

  @Test
  public void findVariableNameWithoutVar() {
    converter = new ToObjectConverter("foo   \n  = \n");
    assertEquals("foo", converter.getVarName());
  }

  @Test
  public void shouldConvertToObject() {
    String code = converter.getObjectDeclaration();

    String expected = "var theThing = {\n" +
        "  first: 'Jackie',\n" +
        "  last: 'Chan',\n" +
        "  phone: {\n" +
        "  areaCode: 212,\n" +
        "  phone: '123-4567'\n" +
        "}\n" +
        "};";
    assertEquals(expected, code);
  }

  @Test
  public void shouldIgnoreMalformedCode() {
    String code = "\n" +
        "var theThing = {\n" +
        "  fist: 'Jackie',\n" +
        "  last: 'Chan',\n" +
        "  phone: {\n" +
        "        areacode: 212,\n" +
        "        phone: '123-4567'\n" +
        "    }\n" +
        "};";

    converter = new ToObjectConverter(code);

    assertNull(converter.getObjectDeclaration());
  }
}
