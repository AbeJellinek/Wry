package org.wrylang.ast;

import org.wrylang.parser.Position;

public class VarExpr extends Expr {
    private Expr name;
    private Expr defaultValue;
    private boolean mutable;

    public VarExpr(Position position, Expr name, Expr defaultValue, boolean mutable) {
        super(position);
        this.name = name;
        this.defaultValue = defaultValue;
        this.mutable = mutable;
    }

    @Override
    public String asString() {
        return "var " + name + " = " + defaultValue;
    }

    public Expr getName() {
        return name;
    }

    public Expr getDefaultValue() {
        return defaultValue;
    }

    public boolean isMutable() {
        return mutable;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
