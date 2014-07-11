package org.wrylang.interpreter;

import org.wrylang.ast.Expr;
import org.wrylang.parser.Elider;
import org.wrylang.parser.Lexer;
import org.wrylang.parser.Parser;
import org.wrylang.parser.SourceReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Interpreter {
    private final Scope scope;

    public Interpreter(Scope scope) {
        this.scope = scope;
    }

    public Obj interpret(Expr expr) {
        return expr.accept(scope);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Interpreter interpreter = new Interpreter(new Scope());
        Lexer lexer = new Lexer(new SourceReader(new FileReader(args[0])));
        Parser parser = new Parser(new Elider(lexer));

        Expr expr;
        while ((expr = parser.next()) != null) {
            interpreter.interpret(expr);
        }
    }
}
