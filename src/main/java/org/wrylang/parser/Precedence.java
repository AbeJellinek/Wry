package org.wrylang.parser;

public class Precedence {
    /**
     * a = b
     */
    public static final int ASSIGNMENT = 1;

    /**
     * a ? b : c
     */
    public static final int TERNARY = 2;

    /**
     * a || b
     * a && b
     */
    public static final int LOGICAL_OP = 3;

    /**
     * a | b
     * a ^ b
     * a & b
     */
    public static final int BITWISE_OP = 4;

    /**
     * a == b
     * a != b
     */
    public static final int EQUALITY = 5;

    /**
     * a < b
     * a > b
     * a <= b
     * a >= b
     * a is B
     */
    public static final int COMPARISON = 6;

    /**
     * a << b
     * a >> b
     * a >>> b
     */
    public static final int SHIFT = 7;

    /**
     * a + b
     * a - b
     */
    public static final int ADDITIVE = 8;

    /**
     * a * b
     * a / b
     * a % b
     */
    public static final int MULTIPLICATIVE = 9;

    /**
     * ++a
     * +a
     * !a
     */
    public static final int PREFIX = 10;

    /**
     * a++
     * a--
     */
    public static final int POSTFIX = 11;

    /**
     * a.b
     */
    public static final int SELECT = 12;
}
