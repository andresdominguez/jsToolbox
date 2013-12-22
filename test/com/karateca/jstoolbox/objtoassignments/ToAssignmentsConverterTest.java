package com.karateca.jstoolbox.objtoassignments;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Andres Dominguez.
 */
public class ToAssignmentsConverterTest {

  public static final String OBJECT_STRING = "var foo = {\n" +
      "    one: 'xxx',\n" +
      "    two: 123,\n" +
      "    'three': 'three',\n" +
      "    'person': {\n" +
      "        name: 'John',\n" +
      "        lastName: 'Doe'\n" +
      "    }\n" +
      "}";
  private ToAssignmentsConverter converter;

  @Before
  public void createTransformer() {
    converter = new ToAssignmentsConverter(OBJECT_STRING);
  }

  @Test
  public void testToAssignments() {
    String expected = "var foo = {};\n" +
        "foo.one = 'xxx';\n" +
        "foo.two = 123;\n" +
        "foo.three = 'three';\n" +
        "foo.person = {\n" +
        "        name: 'John',\n" +
        "        lastName: 'Doe'\n" +
        "    };\n";

    assertEquals(expected, converter.toAssignments());
  }
}
