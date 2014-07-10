package org.wrylang.ast;

import org.wrylang.parser.Position;

public class StringExpr extends Expr {
    private final String value;

    public StringExpr(Position position, String value) {
        super(position);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
