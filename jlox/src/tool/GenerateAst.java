package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {

    static String TAB = "    ";
    static String TAB2 = TAB + TAB;
    static String TAB3 = TAB + TAB + TAB;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
            "Assign   : Token name, Expr value",
            "Binary   : Expr left, Token operator, Expr right",
            "Call     : Expr callee, Token paren, List<Expr> arguments",
            "Get      : Expr object, Token name",
            "Grouping : Expr expression",
            "Lambda   : List<Token> params, List<Stmt> body",
            "Literal  : Object value",
            "Logical  : Expr left, Token operator, Expr right",
            "Set      : Expr object, Token name, Expr value",
            "Super    : Token keyword, Token method",
            "Ternary  : Expr left, Expr mid, Expr right",
            "This     : Token keyword",
            "Unary    : Token operator, Expr right",
            "Variable : Token name"
        ));

        defineAst(outputDir, "Stmt", Arrays.asList(
            "Block      : List<Stmt> statements",
            "Break      : Token keyword",
            "Class      : Token name, Expr.Variable superclass, List<Stmt.Function> methods",
            "Expression : Expr expression",
            "Function   : Token name, List<Token> params, List<Stmt> body",
            "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
            "Print      : Expr expression",
            "Return     : Token keyword, Expr value",
            "Var        : Token name, Expr initializer",
            "While      : Expr condition, Stmt body"
        ));
    }

    private static void defineAst(
        String outputDir, String baseName, List<String> types
    ) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package jlox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        // The AST classes.
        for (String type : types) {
            writer.println();
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        // The base accept() method.
        writer.println();
        writer.println(TAB + "abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineType(
      PrintWriter writer, String baseName,
      String className, String fieldList
    ) {
        writer.println(TAB + "static class " + className + " extends " + baseName + " {");

        // Constructor.
        writer.println(TAB2 + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println(TAB3 + "this." + name + " = " + name + ";");
        }

        writer.println(TAB2 + "}");

        // Visitor pattern.
        writer.println();
        writer.println(TAB2 + "@Override");
        writer.println(TAB2 + "<R> R accept(Visitor<R> visitor) {");
        writer.println(TAB3 + "return visitor.visit" + className + baseName + "(this);");
        writer.println(TAB2 + "}");

        // Fields.
        writer.println();
        for (String field : fields) {
            writer.println(TAB2 + "final " + field + ";");
        }

        writer.println(TAB + "}");
    }

    private static void defineVisitor(
        PrintWriter writer, String baseName, List<String> types
    ) {
        writer.println(TAB + "interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println(TAB2 + "R visit" + typeName + baseName + "(" +
                typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println(TAB + "}");
    }
}