package org.wrylang.parser;

import org.wrylang.ast.DefExpr;
import org.wrylang.ast.Expr;
import org.wrylang.ast.NameExpr;

import java.util.ArrayList;
import java.util.List;

public class DefParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        String name = parser.consume(TokenType.NAME).getText();
        parser.consume(TokenType.LEFT_PAREN);
        List<String> params = new ArrayList<>();

        while (!parser.peek().is(TokenType.RIGHT_PAREN)) {
            Expr next = parser.next();
            params.add(((NameExpr) next).getName());

            if (!parser.peek().is(TokenType.RIGHT_PAREN)) {
                parser.consume(TokenType.COMMA);
            }
        }

        parser.consume(TokenType.RIGHT_PAREN);

        Expr body;
        if (parser.peek().is(TokenType.DO)) {
            body = parser.next();
        } else {
            parser.consume(TokenType.IS);
            body = parser.next();
        }

        parser.endStatement();

        return new DefExpr(token.getPosition(), name, params, body);
    }
}
