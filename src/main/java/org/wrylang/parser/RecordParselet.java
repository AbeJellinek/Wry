package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.RecordExpr;

import java.util.HashMap;
import java.util.Map;

public class RecordParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        Map<String, Expr> fields = new HashMap<>();
        while (!parser.peek().is(TokenType.RIGHT_BRACE)) {
            Token key = parser.consume();
            String keyName;
            if (key.is(TokenType.NAME)) {
                keyName = key.getText();
            } else if (key.is(TokenType.STRING)) {
                keyName = key.getValue();
            } else {
                throw new ParseException("Expected name or string.", key.getPosition());
            }

            parser.match(TokenType.COLON);
            Expr value = parser.next();
            parser.match(TokenType.COMMA);

            fields.put(keyName, value);
        }
        parser.consume(TokenType.RIGHT_BRACE);
        return new RecordExpr(token.getPosition(), fields);
    }
}
