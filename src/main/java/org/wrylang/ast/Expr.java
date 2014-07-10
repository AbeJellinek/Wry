package org.wrylang.ast;

import org.wrylang.parser.Position;

public abstract class Expr {
    private Position position;
    private String doc;

    protected Expr(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        if (doc == null) {
            return asString();
        } else {
            return "/** " + doc + " */ " + asString();
        }
    }

    public abstract String asString();

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public abstract <R> R accept(ExprVisitor<R> visitor);

    public Position getPosition() {
        return position;
    }
}
