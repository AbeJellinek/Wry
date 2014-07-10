package org.wrylang.parser;

public class Position {
    private String sourceFile;
    private int line;
    private int col;

    public Position(String sourceFile, int line, int col) {
        this.sourceFile = sourceFile;
        this.line = line;
        this.col = col;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return String.format("%s (line %d, col %d)", sourceFile, line, col);
    }

    public static final Position NONE = new Position("<unknown>", -1, -1) {
        @Override
        public String toString() {
            return "<unknown>";
        }
    };
}
