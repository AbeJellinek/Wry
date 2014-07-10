package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.NameExpr;
import org.wrylang.ast.SelectExpr;

public class SelectParselet implements InfixParselet {
    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        Expr right = parser.next(Precedence.SELECT);
        if (!(right instanceof NameExpr)) {
            throw new ParseException("Expected name.", right.getPosition());
        }

        return new SelectExpr(token.getPosition(), left, token, (NameExpr) right);
    }

    @Override
    public int getPrecedence() {
        return Precedence.SELECT;
    }
}
