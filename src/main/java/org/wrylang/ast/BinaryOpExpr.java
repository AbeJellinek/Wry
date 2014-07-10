package org.wrylang.ast;

import org.wrylang.parser.Position;
import org.wrylang.parser.Token;

public class BinaryOpExpr extends Expr {
    private Expr left;
    private Token token;
    private Expr right;

    public BinaryOpExpr(Position position, Expr left, Token token, Expr right) {
        super(position);
        this.left = left;
        this.token = token;
        this.right = right;
    }

    @Override
    public String asString() {
        return left + " " + token + " " + right;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public Token getToken() {
        return token;
    }
}
