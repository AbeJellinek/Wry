package org.wrylang.interpreter;

import jline.console.ConsoleReader;
import org.wrylang.ast.DefExpr;
import org.wrylang.ast.Expr;
import org.wrylang.ast.VarExpr;
import org.wrylang.parser.*;

import java.io.IOException;
import java.io.StringReader;

public class Repl {
    public static void main(String[] args) throws IOException {
        Scope scope = new Scope();
        ConsoleReader reader = new ConsoleReader();
        boolean run = true;
        int n = 0;

        reader.println("Wry REPL");
        reader.println("  v1.0  ");

        while (run) {
            String line = reader.readLine("> ");

            if (line != null) {
                if (line.startsWith(":")) {
                    switch (line) {
                        case ":exit":
                        case ":quit":
                            run = false;
                            break;
                    }
                } else {
                    while (line.trim().endsWith("\\")) {
                        line += reader.readLine("> ");
                    }

                    Lexer lexer = new Lexer(new SourceReader(new StringReader(line)));
                    Parser parser = new Parser(new Elider(lexer));

                    try {
                        Expr expr;
                        while ((expr = parser.next()) != null) {
                            Obj result = expr.accept(scope);

                            if (expr instanceof VarExpr || expr instanceof DefExpr) {
                                reader.println(String.valueOf(result));
                            } else {
                                String name = "res" + n++;
                                scope.scopes.peek().addField(name, result, false);
                                reader.println("val " + name + " = " + result);
                            }
                        }
                    } catch (ParseException e) {
                        reader.println(e.getLocalizedMessage());
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        reader.println();
                    }
                }
            } else {
                break;
            }
        }
    }
}
