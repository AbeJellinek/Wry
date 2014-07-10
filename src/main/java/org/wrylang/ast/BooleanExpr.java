package org.wrylang.ast;

import org.wrylang.parser.Position;

public class BooleanExpr extends Expr {
    private boolean value;

    public BooleanExpr(Position position, boolean value) {
        super(position);
        this.value = value;
    }

    public boolean getValue() {
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
