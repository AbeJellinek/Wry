package org.wrylang.parser;

public class Token {
    private Position position;
    private TokenType type;
    private String text;
    private Object value;

    public Token(Position position, TokenType type, String text, Object value) {
        this.position = position;
        this.type = type;
        this.text = text;
        this.value = value;
    }

    public Position getPosition() {
        return position;
    }

    public TokenType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    /**
     * Get the value as type T.
     *
     * @param <T> The value type.
     * @return The value as type T.
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    public boolean is(TokenType type) {
        return this.type == type;
    }

    public boolean is(TokenType type1, TokenType type2) {
        return type == type1 || type == type2;
    }

    public boolean is(TokenType type1, TokenType type2, TokenType type3) {
        return type == type1 || type == type2 || type == type3;
    }

    @Override
    public String toString() {
        return text;
    }

    public boolean isWhitespace() {
        return this.is(TokenType.WHITESPACE) || this.is(TokenType.LINE);
    }
}
