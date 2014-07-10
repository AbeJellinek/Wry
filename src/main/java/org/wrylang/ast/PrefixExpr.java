package org.wrylang.ast;

import org.wrylang.parser.Position;
import org.wrylang.parser.Token;

public class PrefixExpr extends Expr {
    private Token token;
    private Expr operand;

    public PrefixExpr(Position position, Token token, Expr operand) {
        super(position);
        this.token = token;
        this.operand = operand;
    }

    public Token getToken() {
        return token;
    }

    public Expr getOperand() {
        return operand;
    }

    @Override
    public String asString() {
        return String.valueOf(token) + "(" + operand + ")";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
