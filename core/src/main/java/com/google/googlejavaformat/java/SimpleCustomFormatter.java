package com.google.googlejavaformat.java;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePathScanner;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;

/**
 * A formatting base that uses google-java-format utilities but bypasses its layout engine.
 */
public class SimpleCustomFormatter {

  public String format(String source) throws Exception {
    Context context = new Context();
    List<Diagnostic<? extends JavaFileObject>> diagnostics = new ArrayList<>();

    // 1. Use the project's utility to parse the code
    JCCompilationUnit unit = Trees.parse(context, diagnostics, false, source);
    if (!diagnostics.isEmpty()) {
      throw new Exception("Parsing failed");
    }

    StringBuilder out = new StringBuilder();

    // 2. Use a standard OpenJDK scanner to walk the code
    new TreePathScanner<Void, Void>() {
      @Override
      public Void visitClass(ClassTree node, Void unused) {
        out.append("custom-style-class ").append(node.getSimpleName()).append(" {\n");
        super.visitClass(node, null);
        out.append("}\n");
        return null;
      }

      @Override
      public Void visitMethod(MethodTree node, Void unused) {
        out.append("  void ").append(node.getName()).append("() {}\n");
        return null;
      }
    }.scan(unit, null);

    return out.toString();
  }
}
