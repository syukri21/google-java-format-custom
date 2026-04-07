# Project Migration: Custom Style Implementation

This document outlines the steps to remove the default Google Java Style implementation and replace it with your own custom style, while still leveraging the core `google-java-format` formatting engine.

## 1. Goal
The goal is to decouple the **Formatting Decisions** (contained in the AST visitor) from the **Formatting Engine** (the Oppen/Nelson pretty-printer core).

## 2. Core Components to Replace

### A. The AST Visitor (`JavaInputAstVisitor.java`)
This is the most critical file. It contains the logic for every Java language construct (classes, methods, loops, etc.) and decides where to group elements, where to put potential breaks, and how much to indent.

**Migration Steps:**
1.  **Don't delete immediately:** Use the existing `JavaInputAstVisitor` as a reference for how to call `builder` methods (like `builder.open()`, `builder.close()`, `builder.breakOp()`).
2.  **Create a Skeleton Visitor:** Create a new class (e.g., `CustomJavaInputAstVisitor`) extending `TreePathScanner<Void, Void>`.
3.  **Implement Visit Methods:** Implement the `visitX` methods for the Java constructs you want to format. Start with basic ones like `visitCompilationUnit`, `visitClass`, and `visitMethod`.

### B. Formatter Orchestration (`Formatter.java`)
You need to point the formatter to your new visitor.

**Migration Steps:**
1.  Locate the `format` method in `Formatter.java`.
2.  Change the instantiation of the visitor:
    ```java
    // Replace this:
    // JavaInputAstVisitor visitor = new JavaInputAstVisitor(builder, ...);
    
    // With your custom visitor:
    CustomJavaInputAstVisitor visitor = new CustomJavaInputAstVisitor(builder, ...);
    ```

### C. Style Options (`JavaFormatterOptions.java`)
Define your style and its basic parameters (like indentation).

**Migration Steps:**
1.  Add your custom style to the `Style` enum.
2.  Set your desired indentation multiplier.

## 3. Post-Processing Logic to Re-evaluate

Google Java Style includes several mandatory post-processing steps. You should decide if you want to keep, modify, or remove these:

1.  **`ImportOrderer`**: Reorders imports alphabetically and groups them.
2.  **`RemoveUnusedImports`**: Automatically strips imports that aren't used in the code.
3.  **`ModifierOrderer`**: Enforces a specific order for modifiers (e.g., `public static final`).
4.  **`StringWrapper`**: Wraps long string literals.
5. `JavadocFormatter`: Formats Javadoc comments using a sub-engine.

**Note on Javadoc:** You can ignore Javadoc formatting initially by setting `formatJavadoc(false)` in your `JavaFormatterOptions.Builder`. The `JavaCommentsHelper` will then fallback to simple indentation preservation for comments.


To disable these, check the `formatSourceAndFixImports` and `getFormatReplacements` methods in `Formatter.java`.

## 4. Minimal Implementation Blueprint

If you want a "clean slate," your custom visitor's `visitMethod` might look something like this:

```java
@Override
public Void visitMethod(MethodTree node, Void unused) {
    builder.open(ZERO); // Start a new level
    scan(node.getModifiers(), null);
    scan(node.getReturnType(), null);
    builder.space();
    visit(node.getName());
    builder.token("(");
    // ... handle parameters ...
    builder.token(")");
    scan(node.getBody(), null);
    builder.close(); // End the level
    return null;
}
```

## 5. Summary of Files to Modify

| File Path | Purpose of Change |
| :--- | :--- |
| `core/src/main/java/com/google/googlejavaformat/java/JavaFormatterOptions.java` | Add new `Style` enum and default settings. |
| `core/src/main/java/com/google/googlejavaformat/java/Formatter.java` | Wire in the new Visitor and toggle post-processors. |
| `core/src/main/java/com/google/googlejavaformat/java/JavaInputAstVisitor.java` | (Or your new file) Implement custom formatting logic. |
| `core/src/main/java/com/google/googlejavaformat/java/JavaCommentsHelper.java` | Custom comment handling if needed. |

## 6. Recommendations

-   **Start Small**: Implement formatting for just classes and methods first.
-   **Use Tests**: Create new test cases in `core/src/test/java/com/google/googlejavaformat/java/` to verify your style as you build it.
-   **Leverage `OpsBuilder`**: This utility (used by the visitor) handles the complex work of syncing tokens and comments. Try to keep using it.
