package org.wrylang.ast;

import org.wrylang.parser.Position;

public class WhileExpr extends Expr {
    private Expr condition;
    private Expr body;

    public WhileExpr(Position position, Expr condition, Expr body) {
        super(position);
        this.condition = condition;
        this.body = body;
    }

    @Override
    public String asString() {
        return "while " + condition + " " + body;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expr getCondition() {
        return condition;
    }

    public Expr getBody() {
        return body;
    }
}
