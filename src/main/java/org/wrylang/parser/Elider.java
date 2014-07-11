package org.wrylang.parser;

public class Elider implements TokenReader {
    private TokenReader delegate;
    private boolean eatLines = true;

    public Elider(TokenReader delegate) {
        this.delegate = delegate;
    }

    @Override
    public Token next() {
        while (true) {
            Token token = delegate.next();

            switch (token.getType()) {
                case ERROR:
                    throw (RuntimeException) token.getValue();

                case WHITESPACE:
                    continue;

                case DOC_COMMENT:
                case LEFT_PAREN:
                case LEFT_BRACE:
                case LEFT_BRACKET:
                case PLUS:
                case MINUS:
                case TIMES:
                case DIVIDE:
                case NOT:
                case EQ:
                case EQEQ:
                case DEF:
                case VAR:
                case DOT:
                case GT:
                case LT:
                case ASSOC:
                case IS:
                case DO:
                case COMMA:
                    eatLines = true;
                    break;

                case LINE_CONTINUATION:
                    eatLines = true;
                    continue;

                case LINE:
                    if (eatLines)
                        continue;
                    eatLines = true;
                    break;

                default:
                    eatLines = false;
                    break;
            }

            return token;
        }
    }
}
