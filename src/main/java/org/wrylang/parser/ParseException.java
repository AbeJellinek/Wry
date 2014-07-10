package org.wrylang.parser;

public class ParseException extends RuntimeException {
    public ParseException(String message, Position position) {
        super("Error: " + message + "\nat " + position);
    }
}
