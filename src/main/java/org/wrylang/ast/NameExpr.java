package org.wrylang.ast;

import org.wrylang.parser.Position;

public class NameExpr extends Expr {
    private final String name;

    public NameExpr(Position position, String name) {
        super(position);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String asString() {
        return name;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
