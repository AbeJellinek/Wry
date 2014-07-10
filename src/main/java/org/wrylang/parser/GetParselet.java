package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.GetExpr;

import java.util.ArrayList;
import java.util.List;

public class GetParselet implements InfixParselet {
    @Override
    public Expr parse(Parser parser, Expr left, Token token) {
        List<Expr> items = new ArrayList<>();
        while (!parser.peek().is(TokenType.RIGHT_BRACKET)) {
            items.add(parser.next());
            parser.match(TokenType.COMMA);
            parser.line();
        }
        parser.consume(TokenType.RIGHT_BRACKET);
        return new GetExpr(token.getPosition(), left, items);
    }

    @Override
    public int getPrecedence() {
        return Precedence.POSTFIX;
    }
}
