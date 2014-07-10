package org.wrylang.parser;

import org.wrylang.ast.Expr;

public class DocCommentParselet implements PrefixParselet {
    @Override
    public Expr parse(Parser parser, Token token) {
        Expr next = parser.next(Precedence.PREFIX);
        next.setDoc(token.getText().trim());
        return next;
    }
}
