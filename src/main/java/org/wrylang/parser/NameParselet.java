package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.NameExpr;

public class NameParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        return new NameExpr(token.getPosition(), token.getText());
    }
}
