package org.wrylang.ast;

import com.google.common.base.Joiner;
import org.wrylang.parser.Position;

import java.util.List;

public class BlockExpr extends Expr {
    private List<Expr> body;

    public BlockExpr(Position position, List<Expr> body) {
        super(position);
        this.body = body;
    }

    @Override
    public String asString() {
        return "do " + Joiner.on("; ").join(body) + " end";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public List<Expr> getBody() {
        return body;
    }
}
