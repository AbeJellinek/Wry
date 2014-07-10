package org.wrylang.parser;

import org.wrylang.ast.*;

import java.util.ArrayList;
import java.util.List;

public class DoParselet implements PrefixParselet, InfixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        List<Expr> exprs = new ArrayList<>();

        while (!parser.peek().is(TokenType.END)) {
            exprs.add(parser.next());
            parser.line();
        }

        parser.consume(TokenType.END);

        return new BlockExpr(token.getPosition(), exprs);
    }

    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        if (left instanceof TupleExpr) {
            List<Expr> exprs = new ArrayList<>();

            while (!parser.peek().is(TokenType.END)) {
                exprs.add(parser.next());
                parser.line();
            }

            parser.consume(TokenType.END);

            List<String> params = new ArrayList<>();

            for (Expr item : ((TupleExpr) left).getItems()) {
                if (item instanceof NameExpr) {
                    params.add(((NameExpr) item).getName());
                } else {
                    throw new IllegalArgumentException("All parameters must be names.");
                }
            }

            return new LambdaExpr(token.getPosition(), params, new BlockExpr(token.getPosition(), exprs));
        }

        return null;
    }

    @Override
    public int getPrecedence() {
        return Precedence.PREFIX;
    }
}
