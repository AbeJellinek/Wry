package org.wrylang.ast;

import org.wrylang.parser.Position;

import java.util.Map;

public class RecordExpr extends Expr {
    private Map<String, Expr> fields;

    public RecordExpr(Position position, Map<String, Expr> fields) {
        super(position);
        this.fields = fields;
    }

    @Override
    public String asString() {
        return "{...}";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public Map<String, Expr> getFields() {
        return fields;
    }
}
