package org.wrylang.parser;

import org.wrylang.ast.*;

import java.util.ArrayList;
import java.util.List;

public class AssocParselet implements InfixParselet {
    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        List<String> params = new ArrayList<>();

        if (left instanceof TupleExpr) {
            for (Expr item : ((TupleExpr) left).getItems()) {
                if (item instanceof NameExpr) {
                    params.add(((NameExpr) item).getName());
                } else {
                    throw new IllegalArgumentException("All parameters must be names.");
                }
            }
        } else if (left instanceof NameExpr) {
            params.add(((NameExpr) left).getName());
        }

        return new LambdaExpr(token.getPosition(), params, parser.next(Precedence.PREFIX));
    }

    @Override
    public int getPrecedence() {
        return Precedence.PREFIX;
    }
}
