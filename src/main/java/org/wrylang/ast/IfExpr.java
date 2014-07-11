package org.wrylang.ast;

import org.wrylang.parser.Position;

public class IfExpr extends Expr {
    private Expr condition;
    private Expr thenClause;
    private Expr elseClause;

    public IfExpr(Position position, Expr condition, Expr thenClause, Expr elseClause) {
        super(position);
        this.condition = condition;
        this.thenClause = thenClause;
        this.elseClause = elseClause;
    }

    @Override
    public String asString() {
        return "if " + condition + " then " + thenClause + (elseClause != null ? " else " + elseClause : "end");
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expr getCondition() {
        return condition;
    }

    public Expr getThenClause() {
        return thenClause;
    }

    public Expr getElseClause() {
        return elseClause;
    }
}
