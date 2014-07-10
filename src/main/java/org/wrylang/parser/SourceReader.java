package org.wrylang.parser;

import java.io.IOException;
import java.io.Reader;

public class SourceReader {
    private Reader reader;
    private char current;
    private int line;
    private int col;

    public SourceReader(Reader reader) {
        this.reader = reader;
        advance();
    }

    public void advance() {
        try {
            int i = reader.read();
            if (i == -1) {
                current = '\0';
                return;
            }

            current = (char) i;
            if (current == '\n') {
                line++;
                col = 1;
            } else {
                col++;
            }
        } catch (IOException e) {
            current = '\0';
        }
    }

    public char current() {
        return current;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }
}
