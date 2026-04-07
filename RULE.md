# Rules: Custom Style Development

Follow these rules to keep the project maintainable and separate from Google Style.

## 1. Do Not Modify `core` Engine Files
Never modify files in `com.google.googlejavaformat` (like `Doc.java`, `Indent.java`) unless you are fixing a bug in a low-level utility.

## 2. Custom Logic Isolation
All custom formatting logic must live in new classes (e.g., `SimpleCustomFormatter.java`). Do not subclass `JavaInputAstVisitor` if you want a clean break from Google Style.

## 3. Test Isolation
Create your own test files. Do not add tests to `FormatterTest.java` or other Google-specific test suites. Use `-Dtest=YourTestName` to run only your logic.

## 4. Utility Usage
Always prefer the built-in utilities for:
*   **Parsing:** `Trees.parse(...)`
*   **Comments:** `JavaCommentsHelper` (if needed) or direct extraction from `JavaInput`.
*   **Positions:** `Trees.getStartPosition(node)` and `Trees.getEndPosition(node, compilationUnit)`.

## 5. Direct String Building
Since we are bypassing the layout engine, we are responsible for:
*   Managing current indentation level.
*   Inserting newlines.
*   Enforcing our own line length limits.
