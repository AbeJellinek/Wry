package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.TupleExpr;

import java.util.ArrayList;
import java.util.List;

public class TupleParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        List<Expr> items = new ArrayList<>();
        while (!parser.peek().is(TokenType.RIGHT_PAREN)) {
            items.add(parser.next());
            parser.match(TokenType.COMMA);
            parser.line();
        }
        parser.consume(TokenType.RIGHT_PAREN);
        return new TupleExpr(token.getPosition(), items);
    }
}
