package org.wrylang.parser;

import org.wrylang.ast.Expr;

public interface InfixParselet {
    public Expr parse(Parser parser, Expr left, Token token);

    public int getPrecedence();
}
