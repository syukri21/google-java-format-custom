# Approach: Utility-First Custom Formatter

Instead of fighting the complex, highly-coupled Google Java Style engine, we use this project as a **Utility Library**.

## 1. Strategy
We treat `google-java-format` as a provider of low-level Java processing capabilities:
*   **Parsing:** Use `Trees.java` to get a reliable AST from source code.
*   **Tokenization:** Use `JavacTokens.java` or `JavaInput.java` to handle the relationship between AST nodes and original source text.
*   **AST Walking:** Use standard OpenJDK `TreePathScanner` to navigate the code.
*   **Output:** Implement our own `StringBuilder` or specialized `Writer` logic to emit the new style directly.

## 2. Decoupling
We explicitly **ignore** the following core "Engine" classes to avoid Google-style inheritance:
*   `Doc.java` (The Oppen/Nelson layout model)
*   `OpsBuilder.java` (The operation stream)
*   `JavaInputAstVisitor.java` (The Google-style implementation)
*   `JavaOutput.java` (The surgical replacement logic)

## 4. CLI Integration
We use a unified `CustomFormatterCLI` driver to apply our standardized formatting.
1.  **Standardization:** All Java code formatted through this CLI adheres to our single, defined style.
2.  **Execution:** Use the following command to run the formatter (requires `--add-exports` due to JDK compiler API restrictions):

```bash
echo "YOUR_JAVA_CODE" | java \
  --add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED \
  --add-exports jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED \
  --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
  --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
  --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
  --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED \
  -cp core/target/google-java-format-HEAD-SNAPSHOT-all-deps.jar \
  com.google.googlejavaformat.java.CustomFormatterCLI
```

