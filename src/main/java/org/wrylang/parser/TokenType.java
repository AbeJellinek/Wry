package org.wrylang.parser;

public enum TokenType {
    WHITESPACE("whitespace"),
    LINE_CONTINUATION("backslash"),
    LINE("end-of-line"),
    EOF("end-of-file"),
    DOC_COMMENT("doc comment"),
    VAR("var"),
    VAL("val"),
    DEF("def"),
    IS("`is`"),
    DO("`do`"),
    THEN("`then`"),
    IF("`if`"),
    ELSE("`else`"),
    WHILE("`if`"),
    END("`end`"),
    NAME("name"),
    LEFT_PAREN("left paren"),
    RIGHT_PAREN("right paren"),
    LEFT_BRACE("left brace"),
    RIGHT_BRACE("right brace"),
    LEFT_BRACKET("left bracket"),
    RIGHT_BRACKET("right bracket"),
    PLUS("`+`"),
    MINUS("`-`"),
    TIMES("`*`"),
    DIVIDE("`/`"),
    GT("`>`"),
    LT("`<`"),
    GT_EQ("`>=`"),
    LT_EQ("`<=`"),
    ASSOC("`->`"),
    NOT("`!`"),
    NUMBER("number"),
    EQ("`=`"),
    EQEQ("`==`"),
    BANG_EQ("`!=`"),
    AND("`&`"),
    OR("`|`"),
    ANDAND("`&&`"),
    OROR("`||`"),
    DOT("`.`"),
    COMMA("`,`"),
    BOOLEAN("boolean"),
    NULL("null"),
    STRING("string"),
    COLON("`:`"),
    CLASS("class"),
    ERROR("error");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
