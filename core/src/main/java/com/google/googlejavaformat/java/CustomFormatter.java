package com.google.googlejavaformat.java;

/**
 * Interface for all custom formatters.
 */
public interface CustomFormatter {
  /**
   * Formats the given source code.
   * 
   * @param source The raw Java source code.
   * @return The formatted source code.
   * @throws FormatterException If parsing or formatting fails.
   */
  String format(String source) throws FormatterException;
}
