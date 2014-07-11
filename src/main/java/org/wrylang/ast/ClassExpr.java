package org.wrylang.ast;

import org.wrylang.parser.Position;

import java.util.List;

public class ClassExpr extends Expr {
    private String name;
    private List<DefExpr> body;
    private String superClass;

    public ClassExpr(Position position, String name, List<DefExpr> body, String superClass) {
        super(position);
        this.name = name;
        this.body = body;
        this.superClass = superClass;
    }

    public String getName() {
        return name;
    }

    @Override
    public String asString() {
        return "class " + name + " do " + body + " end";
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public List<DefExpr> getBody() {
        return body;
    }

    public String getSuperClass() {
        return superClass;
    }
}
