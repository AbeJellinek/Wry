package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.IfExpr;

public class IfParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        Expr condition = parser.next();
        parser.line();

        parser.match(TokenType.THEN); // consume any wayward "then"s
        parser.line();
        Expr thenClause = parser.next();
        parser.line();

        Expr elseClause = null;
        if (parser.match(TokenType.ELSE)) {
            elseClause = parser.next();
        }

        return new IfExpr(token.getPosition(), condition, thenClause, elseClause);
    }
}
