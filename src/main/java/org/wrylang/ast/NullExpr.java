package org.wrylang.ast;

import org.wrylang.parser.Position;

public class NullExpr extends Expr {
    public NullExpr(Position position) {
        super(position);
    }

    @Override
    public String asString() {
        return "null";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
