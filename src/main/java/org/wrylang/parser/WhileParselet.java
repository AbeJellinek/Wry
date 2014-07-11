package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.WhileExpr;

public class WhileParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        Expr condition = parser.next();
        parser.line();

        parser.line();
        Expr body = parser.next();
        parser.line();

        return new WhileExpr(token.getPosition(), condition, body);
    }
}
