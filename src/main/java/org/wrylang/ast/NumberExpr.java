package org.wrylang.ast;

import org.wrylang.parser.Position;

public class NumberExpr extends Expr {
    private int value;

    public NumberExpr(Position position, int value) {
        super(position);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String asString() {
        return String.valueOf(value);
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
