package org.wrylang.ast;

import com.google.common.base.Joiner;
import org.wrylang.parser.Position;

import java.util.List;

public class CallExpr extends Expr {
    private Expr left;
    private List<Expr> args;

    public CallExpr(Position position, Expr left, List<Expr> args) {
        super(position);
        this.left = left;
        this.args = args;
    }

    @Override
    public String asString() {
        return left + "(" + Joiner.on(", ").join(args) + ")";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expr getLeft() {
        return left;
    }

    public List<Expr> getArgs() {
        return args;
    }
}
