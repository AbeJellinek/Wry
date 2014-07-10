package org.wrylang.parser;

import java.util.HashMap;
import java.util.Map;

public class Lexer implements TokenReader {
    private SourceReader reader;
    private StringBuilder read = new StringBuilder();
    private int startLine = 0;
    private int startCol = 0;
    private Map<String, TokenType> keywordMap = new HashMap<>();

    public Lexer(SourceReader reader) {
        this.reader = reader;

        keywordMap.put("var", TokenType.VAR);
        keywordMap.put("val", TokenType.VAL);
        keywordMap.put("def", TokenType.DEF);
        keywordMap.put("is", TokenType.IS);
        keywordMap.put("do", TokenType.DO);
        keywordMap.put("end", TokenType.END);
        keywordMap.put("true", TokenType.BOOLEAN);
        keywordMap.put("false", TokenType.BOOLEAN);
        keywordMap.put("null", TokenType.NULL);
        keywordMap.put("if", TokenType.IF);
        keywordMap.put("while", TokenType.WHILE);
        keywordMap.put("class", TokenType.CLASS);
    }

    @Override
    public Token next() {
        char c = consume();

        switch (c) {
            case ' ':
            case '\t':
                return readWhitespace();

            case '(':
                return token(TokenType.LEFT_PAREN);
            case ')':
                return token(TokenType.RIGHT_PAREN);
            case '[':
                return token(TokenType.LEFT_BRACKET);
            case ']':
                return token(TokenType.RIGHT_BRACKET);
            case '{':
                return token(TokenType.LEFT_BRACE);
            case '}':
                return token(TokenType.RIGHT_BRACE);

            case ':':
                return token(TokenType.COLON);

            case ',':
                return token(TokenType.COMMA);

            case '=':
                if (peek() == '=') {
                    consume();
                    return token(TokenType.EQEQ);
                } else {
                    return token(TokenType.EQ);
                }

            case '\n':
            case '\r':
            case ';':
                return token(TokenType.LINE);

            case '\\':
                return token(TokenType.LINE_CONTINUATION);

            case '\0':
                return token(TokenType.EOF);

            case '/':
                if (peek() == '*') {
                    consume();
                    return readBlockComment();
                } else if (peek() == '/') {
                    consume();
                    return readLineComment();
                } else {
                    return readOperator();
                }

            case '-':
                if (Character.isDigit(peek())) {
                    return readNumber();
                } else {
                    return readOperator();
                }

            case '.':
                return token(TokenType.DOT);

            case '"':
                return readString();

            default:
                if (isIdentifierStartChar(c)) {
                    return readName();
                } else if (isOperatorChar(c)) {
                    return readOperator();
                } else if (Character.isDigit(c)) {
                    return readNumber();
                } else {
                    return error("Unexpected character: " + c);
                }
        }
    }

    private void unread(int n) {
        read.setLength(read.length() - n);
    }

    private Token readWhitespace() {
        while (peek() == ' ' || peek() == '\t') {
            consume();
        }

        return token(TokenType.WHITESPACE);
    }

    private Token readName() {
        while (isIdentifierPartChar(peek())) {
            consume();
        }

        String s = read.toString();
        if (keywordMap.containsKey(s)) {
            return token(keywordMap.get(s));
        } else {
            return token(TokenType.NAME);
        }
    }

    private Token readBlockComment() {
        boolean doc = peek() == '*';
        if (doc) {
            consume();
        }

        read.setLength(0);

        while (true) {
            char next = consume();
            if (next == '\0') {
                return error("Unterminated comment.");
            } else if (next == '*' && peek() == '/') {
                consume(); // Consume the /.
                unread(2);
                break;
            }
        }

        if (doc) {
            return token(TokenType.DOC_COMMENT);
        } else {
            return token(TokenType.WHITESPACE); // Comments are parsed as whitespace for now.
        }
    }

    private Token readLineComment() {
        while (true) {
            char next = consume();
            if (next == '\0' || next == '\n') {
                break;
            }
        }

        return token(TokenType.WHITESPACE);
    }

    private Token readOperator() {
        switch (read.toString()) {
            case "+":
                return token(TokenType.PLUS);
            case "-":
                if (peek() == '>') {
                    consume();
                    return token(TokenType.ASSOC);
                } else {
                    return token(TokenType.MINUS);
                }
            case "*":
                return token(TokenType.TIMES);
            case "/":
                return token(TokenType.DIVIDE);
            case "!":
                if (peek() == '=') {
                    consume();
                    return token(TokenType.BANG_EQ);
                } else {
                    return token(TokenType.NOT);
                }
            case "&":
                if (peek() == '&') {
                    consume();
                    return token(TokenType.ANDAND);
                } else {
                    return token(TokenType.AND);
                }
            case "|":
                if (peek() == '|') {
                    consume();
                    return token(TokenType.OROR);
                } else {
                    return token(TokenType.OR);
                }
            default:
                return error("Invalid operator: " + read);
        }
    }

    private Token readNumber() {
        while (Character.isDigit(peek())) {
            consume();
        }

        String readString = read.toString();
        int num;
        try {
            num = Integer.parseInt(readString);
        } catch (NumberFormatException e) {
            return error("Invalid number: " + readString);
        }

        return token(TokenType.NUMBER, num);
    }

    private Token readString() {
        StringBuilder acc = new StringBuilder();

        while (true) {
            char c = consume();
            if (c == '\\') {
                char escape = consume();
                switch (escape) {
                    case 't':
                        acc.append("\t");
                    case 'b':
                        acc.append("\b");
                    case 'n':
                        acc.append("\n");
                    case 'r':
                        acc.append("\r");
                    case 'f':
                        acc.append("\f");
                    case '\'':
                        acc.append("\'");
                    case '"':
                        acc.append("\"");
                    case '\\':
                        acc.append("\\");
                    default:
                        return error("Invalid escape code.");
                }
            } else if (c == '"') {
                break;
            } else {
                acc.append(c);
            }
        }

        return token(TokenType.STRING, acc.toString());
    }

    private char consume() {
        char c = reader.current();
        reader.advance();
        read.append(c);
        return c;
    }

    private char peek() {
        return reader.current();
    }

    private boolean isIdentifierStartChar(char c) {
        // Because Java allows the EOF char in identifiers. Right.
        return c != '\0' && Character.isJavaIdentifierStart(c);
    }

    private boolean isIdentifierPartChar(char c) {
        return c != '\0' && Character.isJavaIdentifierPart(c);
    }

    private boolean isOperatorChar(char c) {
        return "+-*/!&|".indexOf(c) != -1;
    }

    private Token token(TokenType type) {
        Token token = new Token(new Position("<source>", startLine, startCol),
                type, read.toString(), read.toString());
        read.setLength(0);
        startLine = reader.getLine();
        startCol = reader.getCol();
        return token;
    }

    private Token token(TokenType type, Object value) {
        Token token = new Token(new Position("<source>", startLine, startCol),
                type, read.toString(), value);
        read.setLength(0);
        startLine = reader.getLine();
        startCol = reader.getCol();
        return token;
    }

    private Token error(String msg) {
        Position pos = new Position("<source>", startLine, startCol);
        Token token = new Token(pos, TokenType.ERROR, msg, new ParseException(msg, pos));
        read.setLength(0);
        startLine = reader.getLine();
        startCol = reader.getCol();
        return token;
    }
}
