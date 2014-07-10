package org.wrylang.parser;

import org.wrylang.ast.ClassExpr;
import org.wrylang.ast.DefExpr;
import org.wrylang.ast.Expr;

import java.util.ArrayList;
import java.util.List;

public class ClassParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        String name = parser.consume(TokenType.NAME).getText();
        parser.consume(TokenType.DO);
        List<DefExpr> body = new ArrayList<>();
        while (!parser.peek().is(TokenType.END)) {
            Expr next = parser.next();
            if (next instanceof DefExpr) {
                body.add((DefExpr) next);
            } else {
                throw new ParseException("Expected method.", next.getPosition());
            }
        }
        parser.consume(TokenType.END);
        parser.endStatement();

        return new ClassExpr(token.getPosition(), name, body);
    }
}
