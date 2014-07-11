package org.wrylang.parser;

import org.wrylang.ast.Expr;
import org.wrylang.ast.PrefixExpr;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private final Map<TokenType, PrefixParselet> prefixParselets = new HashMap<>();
    private final Map<TokenType, InfixParselet> infixParselets = new HashMap<>();
    private Deque<Token> buffer = new ArrayDeque<>();
    private TokenReader lexer;
    private boolean endStatement;

    public Parser(TokenReader lexer) {
        this.lexer = lexer;

        prefix(TokenType.MINUS);
        prefix(TokenType.PLUS);
        prefix(TokenType.NOT);
        prefix(TokenType.NAME, new NameParselet());
        prefix(TokenType.NUMBER, new LiteralParselet());
        prefix(TokenType.BOOLEAN, new LiteralParselet());
        prefix(TokenType.NULL, new LiteralParselet());
        prefix(TokenType.STRING, new LiteralParselet());
        prefix(TokenType.DOC_COMMENT, new DocCommentParselet());
        prefix(TokenType.VAR, new VarParselet(true));
        prefix(TokenType.VAL, new VarParselet(false));
        prefix(TokenType.DEF, new DefParselet());
        prefix(TokenType.DO, new DoParselet());
        prefix(TokenType.LEFT_PAREN, new TupleParselet());
        prefix(TokenType.LEFT_BRACE, new RecordParselet());
        prefix(TokenType.CLASS, new ClassParselet());

        infix(TokenType.PLUS, Precedence.ADDITIVE);
        infix(TokenType.MINUS, Precedence.ADDITIVE);
        infix(TokenType.TIMES, Precedence.MULTIPLICATIVE);
        infix(TokenType.DIVIDE, Precedence.MULTIPLICATIVE);
        infix(TokenType.EQ, Precedence.ASSIGNMENT, true);
        infix(TokenType.EQEQ, Precedence.EQUALITY);
        infix(TokenType.AND, Precedence.BITWISE_OP);
        infix(TokenType.OR, Precedence.BITWISE_OP);
        infix(TokenType.ANDAND, Precedence.LOGICAL_OP);
        infix(TokenType.OROR, Precedence.LOGICAL_OP);
        infix(TokenType.BANG_EQ, Precedence.EQUALITY);
        infix(TokenType.DO, new DoParselet());
        infix(TokenType.ASSOC, new AssocParselet());
        infix(TokenType.LEFT_PAREN, new CallParselet());
        infix(TokenType.LEFT_BRACKET, new GetParselet());
        infix(TokenType.DOT, new SelectParselet());
    }

    public void prefix(TokenType type) {
        register(type, (parser, token) -> new PrefixExpr(token.getPosition(), token, parser.next(Precedence.PREFIX)));
    }

    public void prefix(TokenType type, PrefixParselet parselet) {
        register(type, parselet);
    }

    public void infix(TokenType token, int precedence) {
        infix(token, precedence, false);
    }

    public void infix(TokenType token, InfixParselet parselet) {
        register(token, parselet);
    }

    public void infix(TokenType token, int precedence, boolean isRight) {
        register(token, new BinaryOpParselet(precedence, isRight));
    }

    public void register(TokenType token, PrefixParselet parselet) {
        prefixParselets.put(token, parselet);
    }

    public void register(TokenType token, InfixParselet parselet) {
        infixParselets.put(token, parselet);
    }

    public Expr next(int precedence) {
        endStatement = false;

        line();
        Token token = consume();

        if (token.is(TokenType.EOF)) {
            return null;
        }

        PrefixParselet prefix = prefixParselets.get(token.getType());
        if (prefix == null) {
            throw new ParseException("Unexpected token: " + token.getText(), token.getPosition());
        }

        Expr left = prefix.parse(this, token);

        while (!endStatement && precedence < getPrecedence()) {
            Token infixToken = consume();

            InfixParselet infix = infixParselets.get(infixToken.getType());
            if (infix == null) {
                throw new ParseException("Unexpected token: " + infixToken.getText(), infixToken.getPosition());
            }
            left = infix.parse(this, left, infixToken);
        }

        return left;
    }

    public void line() {
        while (peek().is(TokenType.LINE)) {
            consume();
        }
    }

    public void endStatement() {
        if (peek().is(TokenType.LINE, TokenType.EOF)) {
            line();
            endStatement = true;
        } else {
            throw new ParseException("Unexpected token: " + peek().getType() +
                    ". Use a semicolon to separate statements on the same line.", peek().getPosition());
        }
    }

    public Expr next() {
        return next(0);
    }

    private int getPrecedence() {
        InfixParselet parser = infixParselets.get(peek().getType());
        if (parser != null) return parser.getPrecedence();

        return -1;
    }

    public Token consume() {
        if (buffer.isEmpty()) {
            return lexer.next();
        } else {
            return buffer.pop();
        }
    }

    public Token consume(TokenType type) {
        Token token = consume();
        if (token.getType() == type) {
            return token;
        } else {
            throw new ParseException("Expected " + type + ", but found " + token.getType() + ".", token.getPosition());
        }
    }

    public boolean match(TokenType type) {
        Token token = peek();
        if (token.getType() == type) {
            consume();
            return true;
        } else {
            return false;
        }
    }

    public Token peek() {
        if (buffer.isEmpty()) {
            Token next = lexer.next();
            buffer.push(next);
            return next;
        } else {
            return buffer.peek();
        }
    }
}
