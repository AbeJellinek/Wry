package org.wrylang.interpreter;

import org.wrylang.ast.Expr;
import org.wrylang.parser.Elider;
import org.wrylang.parser.Lexer;
import org.wrylang.parser.Parser;
import org.wrylang.parser.SourceReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Interpreter {
    public Obj interpret(Scope scope, Expr expr) {
        return expr.accept(scope);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scope scope = new Scope();
        Interpreter interpreter = new Interpreter();
        Lexer lexer = new Lexer(new SourceReader(new FileReader(args[0])));
        Parser parser = new Parser(new Elider(lexer));

        Expr expr;
        while ((expr = parser.next()) != null) {
            interpreter.interpret(scope, expr);
        }
    }
}
