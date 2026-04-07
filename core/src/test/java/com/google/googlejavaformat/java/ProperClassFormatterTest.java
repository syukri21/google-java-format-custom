package com.google.googlejavaformat.java;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProperClassFormatterTest {

  @Test
  public void testFormatSingleLineClass() throws Exception {
    String input = "class A { void test() { return \"hello\"; } }";
    ProperClassFormatter formatter = new ProperClassFormatter(2);
    String output = formatter.format(input);

    String expected = "class A {\n"
        + "  void test() {\n"
        + "    return \"hello\";\n"
        + "  }\n"
        + "}\n";

    assertThat(output).isEqualTo(expected);
  }

  @Test
  public void testMultipleMethods() throws Exception {
      String input = "class B { void f() {} void g(int x) { int y = x; } }";
      ProperClassFormatter formatter = new ProperClassFormatter(2);
      String output = formatter.format(input);

      String expected = 
          "class B {\n" +
          "  void f() {\n" +
          "  }\n" +
          "  void g(int x) {\n" +
          "    int y = x;\n" +
          "  }\n" +
          "}\n";

      assertThat(output).isEqualTo(expected);
  }

}
