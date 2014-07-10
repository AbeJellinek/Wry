package org.wrylang.parser;

import org.wrylang.ast.BinaryOpExpr;
import org.wrylang.ast.Expr;

public class BinaryOpParselet implements InfixParselet {
    private int precedence;
    private boolean isRight;

    public BinaryOpParselet(int precedence, boolean isRight) {
        this.precedence = precedence;
        this.isRight = isRight;
    }

    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        Expr right = parser.next(precedence - (isRight ? 1 : 0));
        if (right == null) {
            throw new ParseException("EOF while parsing.", token.getPosition());
        }
        return new BinaryOpExpr(token.getPosition(), left, token, right);
    }

    @Override
    public int getPrecedence() {
        return precedence;
    }
}
