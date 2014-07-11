package org.wrylang.ast;

public interface ExprVisitor<R> {
    public R visit(BinaryOpExpr expr);

    public R visit(NameExpr expr);

    public R visit(NumberExpr expr);

    public R visit(PrefixExpr expr);

    public R visit(VarExpr expr);

    public R visit(SelectExpr expr);

    public R visit(DefExpr expr);

    public R visit(BlockExpr expr);

    public R visit(TupleExpr expr);

    public R visit(LambdaExpr expr);

    public R visit(CallExpr expr);

    public R visit(BooleanExpr expr);

    public R visit(NullExpr expr);

    public R visit(StringExpr expr);

    public R visit(GetExpr expr);

    public R visit(RecordExpr expr);

    public R visit(ClassExpr expr);

    public R visit(IfExpr expr);

    public R visit(WhileExpr expr);
}
