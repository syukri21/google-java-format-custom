# Project Explanation: google-java-format

This document explains the architecture, structure, and core models of the `google-java-format` project. This information is intended for developers who wish to use this project as a base for custom formatting styles.

## 1. Overview

`google-java-format` is a Java source code formatter that follows the Google Java Style Guide. It is designed to be non-configurable to ensure a consistent style across codebases. It uses the OpenJDK `javac` parser to understand the code's structure and then applies a custom formatting engine to produce the output.

## 2. Architecture

The formatting process follows a "Research -> Strategy -> Execution" lifecycle, mapped to the following stages:

1.  **Parsing (AST Generation):**
    The source code is parsed using the internal OpenJDK compiler (`javac`) API. This produces a `JCCompilationUnit` (Abstract Syntax Tree - AST).
    *   **Key Class:** `com.google.googlejavaformat.java.Trees`

2.  **AST Traversal & Operation Generation:**
    A specialized visitor walks the AST. Instead of directly printing code, it generates a sequence of "Operations" (`Op`).
    *   **Key Class:** `com.google.googlejavaformat.java.JavaInputAstVisitor`
    *   **Key Class:** `com.google.googlejavaformat.OpsBuilder`

3.  **Document Building:**
    The flat stream of operations is converted into a hierarchical "Document" (`Doc`) model. This model represents the structure of the code (blocks, levels, and potential breaks).
    *   **Key Class:** `com.google.googlejavaformat.DocBuilder`
    *   **Key Class:** `com.google.googlejavaformat.Doc`

4.  **Break Calculation:**
    The engine calculates where to actually break lines based on the maximum line length (default 100) and the grouping logic (FillMode) defined in the `Doc`. It optimizes for the best-looking output according to the Oppen/Nelson pretty-printing algorithm.
    *   **Key Method:** `Doc.computeBreaks()`

5.  **Output Generation:**
    Finally, the `Doc` is written to an `Output` object, which applies the calculated breaks and indentation to produce the final string.
    *   **Key Class:** `com.google.googlejavaformat.java.JavaOutput`

## 3. Project Structure

The repository is organized into several modules:

-   **`core/`**: The heart of the formatter.
    -   `src/main/java/com/google/googlejavaformat/`: Contains the language-agnostic core formatting engine (`Doc`, `Op`, `OpsBuilder`, `Indent`, etc.).
    -   `src/main/java/com/google/googlejavaformat/java/`: Contains Java-specific implementations (`JavaInputAstVisitor`, `JavaFormatterOptions`, `ImportOrderer`, etc.).
-   **`eclipse_plugin/`**: Integration for the Eclipse IDE.
-   **`idea_plugin/`**: Integration for IntelliJ IDEA / Android Studio.
-   **`scripts/`**: Utility scripts (e.g., `google-java-format-diff.py`).

## 4. Key Models & Concepts

### Formatting Operations (`Op`)
Formatting is built using a stream of operations:
-   **`TokenOp`**: Represents a literal piece of text (e.g., `public`, `class`, `{`).
-   **`OpenOp` / `CloseOp`**: Define boundaries of a "Level" (a group of elements that should be indented together if broken).
-   **`BreakOp`**: Represents a potential location for a line break.

### The Document Model (`Doc`)
-   **`Doc.Level`**: A collection of elements (Tokens, Breaks, and nested Levels).
-   **`Doc.Break`**: A decision point. If a level exceeds the line length, the engine decides which breaks to "take" (convert to newlines).
-   **`FillMode`**:
    -   `UNIFIED`: If the group doesn't fit on one line, break all breaks in this level.
    -   `INDEPENDENT`: Break each break only if necessary to fit on the line.

### Indentation (`Indent`)
Indentation is managed via `Indent` objects.
-   The base indentation unit in `google-java-format` is usually 2 spaces.
-   `JavaFormatterOptions.Style` provides a multiplier:
    -   `GOOGLE`: Multiplier = 1 (2-space indent).
    -   `AOSP`: Multiplier = 2 (4-space indent).

## 5. Extending for a New Style

To create a new style based on this project:
1.  **Modify `JavaFormatterOptions`**: Add a new `Style` enum entry and define your indentation multiplier.
2.  **Adjust `JavaInputAstVisitor`**: This is the most complex part. You may need to subclass or modify the visitor to change how it groups code into levels or where it inserts breaks.
3.  **Utility Functions**: Use `Trees` for AST manipulation, `Newlines` for line separator detection, and `JavaCommentsHelper` for comment formatting.
