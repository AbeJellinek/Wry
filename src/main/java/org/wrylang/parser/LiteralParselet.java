package org.wrylang.parser;

import org.wrylang.ast.*;

public class LiteralParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        switch (token.getType()) {
            case NUMBER:
                return new NumberExpr(token.getPosition(), token.getValue());
            case BOOLEAN:
                return new BooleanExpr(token.getPosition(), Boolean.parseBoolean(token.getText()));
            case NULL:
                return new NullExpr(token.getPosition());
            case STRING:
                return new StringExpr(token.getPosition(), token.getValue());
            default:
                throw new IllegalArgumentException("Unknown literal type: " + token.getType());
        }
    }
}
