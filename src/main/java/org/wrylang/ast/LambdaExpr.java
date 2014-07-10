package org.wrylang.ast;

import com.google.common.base.Joiner;
import org.wrylang.parser.Position;

import java.util.List;

public class LambdaExpr extends Expr {
    private List<String> params;
    private Expr body;

    public LambdaExpr(Position position, List<String> params, Expr body) {
        super(position);
        this.params = params;
        this.body = body;
    }

    public List<String> getParams() {
        return params;
    }

    @Override
    public String asString() {
        return "(" + Joiner.on(", ").join(params) + ") do " + body + " end";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expr getBody() {
        return body;
    }
}
