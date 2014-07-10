package org.wrylang.parser;

import org.wrylang.ast.ClassExpr;
import org.wrylang.ast.Expr;
import org.wrylang.ast.NameExpr;

import java.util.ArrayList;
import java.util.List;

public class ClassParselet implements PrefixParselet {
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
        parser.consume(TokenType.DO);
        List<Expr> body = new ArrayList<>();
        while (!parser.peek().is(TokenType.END)) {
            body.add(parser.next());
        }
        parser.consume(TokenType.END);
        parser.endStatement();

        return new ClassExpr(token.getPosition(), name, params, body);
    }
}
