package org.wrylang.parser;

import org.wrylang.ast.CallExpr;
import org.wrylang.ast.Expr;
import org.wrylang.ast.TupleExpr;

public class CallParselet extends TupleParselet implements InfixParselet {
    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        return new CallExpr(token.getPosition(), left, ((TupleExpr) parse(parser, token)).getItems());
    }

    @Override
    public int getPrecedence() {
        return Precedence.POSTFIX;
    }
}
