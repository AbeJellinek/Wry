package org.wrylang.ast;

import com.google.common.base.Joiner;
import org.wrylang.parser.Position;

import java.util.List;

public class DefExpr extends Expr {
    private Expr name;
    private List<String> params;
    private Expr body;

    public DefExpr(Position position, Expr name, List<String> params, Expr body) {
        super(position);
        this.name = name;
        this.params = params;
        this.body = body;
    }

    public Expr getName() {
        return name;
    }

    public List<String> getParams() {
        return params;
    }

    @Override
    public String asString() {
        return "def " + name + "(" + Joiner.on(", ").join(params) + ") do " + body + " end";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expr getBody() {
        return body;
    }
}
