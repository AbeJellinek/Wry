package org.wrylang.ast;

import org.wrylang.parser.Position;
import org.wrylang.parser.Token;

public class SelectExpr extends Expr {
    private Expr left;
    private Token token;
    private NameExpr right;

    public SelectExpr(Position position, Expr left, Token token, NameExpr right) {
        super(position);
        this.left = left;
        this.token = token;
        this.right = right;
    }

    @Override
    public String asString() {
        return String.valueOf(left) + token + right;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expr getLeft() {
        return left;
    }

    public NameExpr getRight() {
        return right;
    }

    public Token getToken() {
        return token;
    }
}
