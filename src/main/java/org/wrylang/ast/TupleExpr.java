package org.wrylang.ast;

import com.google.common.base.Joiner;
import org.wrylang.parser.Position;

import java.util.List;

public class TupleExpr extends Expr {
    private List<Expr> items;

    public TupleExpr(Position position, List<Expr> items) {
        super(position);
        this.items = items;
    }

    @Override
    public String asString() {
        return "(" + Joiner.on(", ").join(items) + ")";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public List<Expr> getItems() {
        return items;
    }
}
