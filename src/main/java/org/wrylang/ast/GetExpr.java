package org.wrylang.ast;

import com.google.common.base.Joiner;
import org.wrylang.parser.Position;

import java.util.List;

public class GetExpr extends Expr {
    private Expr left;
    private List<Expr> items;

    public GetExpr(Position position, Expr left, List<Expr> items) {
        super(position);
        this.left = left;
        this.items = items;
    }

    @Override
    public String asString() {
        return left + "[" + Joiner.on(", ").join(items) + "]";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public List<Expr> getItems() {
        return items;
    }

    public Expr getLeft() {
        return left;
    }
}
