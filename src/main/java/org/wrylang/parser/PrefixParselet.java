package org.wrylang.parser;

import org.wrylang.ast.Expr;

public interface PrefixParselet {
    public Expr parse(Parser parser, Token token);
}
