package org.wrylang.interpreter;

import org.wrylang.parser.Position;

public class WryException extends RuntimeException {
    public WryException(Exception cause, Position position) {
        super("Error at " + position, cause);
    }
}
