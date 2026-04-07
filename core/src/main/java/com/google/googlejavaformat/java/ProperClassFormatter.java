package com.google.googlejavaformat.java;

import com.sun.source.tree.*;
import com.sun.source.util.TreePathScanner;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import java.util.ArrayList;
import java.util.List;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * A formatter that converts single-line Java code into a properly indented multi-line format. Follows the Utility-First
 * approach by using the project's parsing capabilities while implementing custom output logic.
 */
public class ProperClassFormatter implements CustomFormatter {

  private final int indentSize;

  public ProperClassFormatter(int indentSize) {
    this.indentSize = indentSize;
  }

  public String format(String source) throws FormatterException {
    Context context = new Context();
    List<Diagnostic<? extends JavaFileObject>> diagnostics = new ArrayList<>();

    JCCompilationUnit unit = Trees.parse(context, diagnostics, false, source);
    if (!diagnostics.isEmpty()) {
      throw FormatterException.fromJavacDiagnostics(diagnostics);
    }

    StringBuilder out = new StringBuilder();
    new ProperScanner(out, indentSize).scan(unit, null);
    return out.toString();
  }

  private static class ProperScanner extends TreePathScanner<Void, Void> {
    private final StringBuilder out;
    private final int indentSize;
    private int currentIndent = 0;

    ProperScanner(StringBuilder out, int indentSize) {
      this.out = out;
      this.indentSize = indentSize;
    }

    private void appendIndent() {
      for (int i = 0; i < currentIndent; i++) {
        out.append(" ");
      }
    }

    private void newline() {
      out.append("\n");
      appendIndent();
    }

    @Override
    public Void visitClass(ClassTree node, Void unused) {
      appendIndent();
      out.append("class ").append(node.getSimpleName()).append(" {");
      currentIndent += indentSize;

      for (Tree member : node.getMembers()) {
        newline();
        scan(member, null);
        if (member instanceof VariableTree) {
          out.append(";");
        }
      }

      currentIndent -= indentSize;
      out.append("\n");
      appendIndent();
      out.append("}\n");
      return null;
    }

    @Override
    public Void visitMethod(MethodTree node, Void unused) {
      if (node.getReturnType() != null) {
        out.append(node.getReturnType()).append(" ");
      }
      out.append(node.getName()).append("(");

      List<? extends VariableTree> parameters = node.getParameters();
      for (int i = 0; i < parameters.size(); i++) {
        scan(parameters.get(i), null);
        if (i < parameters.size() - 1) {
          out.append(", ");
        }
      }
      out.append(") ");

      if (node.getBody() != null) {
        out.append("{");
        currentIndent += indentSize;
        scan(node.getBody(), null);
        currentIndent -= indentSize;
        out.append("\n");
        appendIndent();
        out.append("}");
      } else {
        out.append(";");
      }
      return null;
    }

    @Override
    public Void visitBlock(BlockTree node, Void unused) {
      for (StatementTree statement : node.getStatements()) {
        newline();
        scan(statement, null);
        if (statement instanceof VariableTree) {
          out.append(";");
        }
      }
      return null;
    }

    @Override
    public Void visitVariable(VariableTree node, Void unused) {
      if (node.getType() != null) {
        out.append(node.getType()).append(" ");
      }
      out.append(node.getName());
      if (node.getInitializer() != null) {
        out.append(" = ");
        scan(node.getInitializer(), null);
      }
      return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatementTree node, Void unused) {
      scan(node.getExpression(), null);
      out.append(";");
      return null;
    }

    @Override
    public Void visitReturn(ReturnTree node, Void unused) {
      out.append("return");
      if (node.getExpression() != null) {
        out.append(" ");
        scan(node.getExpression(), null);
      }
      out.append(";");
      return null;
    }

    @Override
    public Void visitLiteral(LiteralTree node, Void unused) {
      Object value = node.getValue();
      if (value instanceof String) {
        out.append("\"").append(value).append("\"");
      } else {
        out.append(value);
      }
      return null;
    }

    @Override
    public Void visitIdentifier(IdentifierTree node, Void unused) {
      out.append(node.getName());
      return null;
    }
  }
}
