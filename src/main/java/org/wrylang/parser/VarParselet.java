package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.NameExpr;
import org.wrylang.ast.SelectExpr;
import org.wrylang.ast.VarExpr;

public class VarParselet implements PrefixParselet {
    private boolean mutable;

    public VarParselet(boolean mutable) {
        this.mutable = mutable;
    }

    @Override
    public Expr parse(Parser parser, Token token) {
        Expr nameExpr = parser.next(Precedence.PREFIX);

        if (!(nameExpr instanceof NameExpr) && !(nameExpr instanceof SelectExpr)) {
            throw new ParseException("Expected a name.", nameExpr.getPosition());
        }

        parser.consume(TokenType.EQ);
        Expr defaultValue = parser.next(Precedence.ASSIGNMENT);

        parser.endStatement();

        return new VarExpr(token.getPosition(), nameExpr, defaultValue, mutable);
    }
}
