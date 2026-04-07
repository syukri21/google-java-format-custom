# Concepts: Core Building Blocks

When building our custom style, we rely on these foundational concepts from the base project.

## 1. The AST (Abstract Syntax Tree)
The project provides access to the OpenJDK internal compiler AST. Every piece of Java code is represented as a `Tree` (e.g., `ClassTree`, `MethodTree`, `BinaryTree`).

## 2. Source Mapping (`JavaInput`)
`JavaInput` is a crucial utility. It holds the original source text and the token stream. It allows us to:
*   Find the start and end position of any `Tree` node.
*   Extract comments associated with specific tokens.
*   Handle line endings correctly.

## 3. Position Synchronization
The concept of "Syncing" is still important. As we walk the AST, we must track our position in the original source to ensure we don't skip comments or important whitespace that should be preserved.

## 4. Style Neutrality
A "Style Neutral" utility is one that doesn't make decisions about indentation, line length, or grouping. We prioritize these utilities over "Style Opinionated" classes.
