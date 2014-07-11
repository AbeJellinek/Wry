package org.wrylang.interpreter;

import org.wrylang.ast.*;
import org.wrylang.parser.Position;

import java.util.*;

public class Scope implements ExprVisitor<Obj> {
    Deque<Obj> scopes = new ArrayDeque<>();

    public Scope() {
        scopes.push(new Obj(null)); // dummy object for intrinsic initialization
        scopes.push(new IntrinsicGenerator().generate(this, new WryIntrinsics(this)));
    }

    private int compare(Expr left, Expr right) {
        return ((NumberObj) left.accept(this).getField("compareTo").invoke(right.accept(this))).getValue();
    }

    @Override
    public Obj visit(BinaryOpExpr expr) {
        switch (expr.getToken().getType()) {
            case PLUS:
                return expr.getLeft().accept(this).getField("plus")
                        .invoke(expr.getRight().accept(this));
            case MINUS:
                return expr.getLeft().accept(this).getField("minus")
                        .invoke(expr.getRight().accept(this));
            case TIMES:
                return expr.getLeft().accept(this).getField("times")
                        .invoke(expr.getRight().accept(this));
            case DIVIDE:
                return expr.getLeft().accept(this).getField("divide")
                        .invoke(expr.getRight().accept(this));
            case EQEQ:
                return new BooleanObj(this, expr.getLeft().accept(this)
                        .equals(expr.getRight().accept(this)));
            case BANG_EQ:
                return new BooleanObj(this, !expr.getLeft().accept(this)
                        .equals(expr.getRight().accept(this)));
            case LT:
                return new BooleanObj(this, compare(expr.getLeft(), expr.getRight()) < 0);
            case GT:
                return new BooleanObj(this, compare(expr.getLeft(), expr.getRight()) > 0);
            case LT_EQ:
                return new BooleanObj(this, compare(expr.getLeft(), expr.getRight()) <= 0);
            case GT_EQ:
                return new BooleanObj(this, compare(expr.getLeft(), expr.getRight()) >= 0);
            case ANDAND:
                return expr.getLeft().accept(this).getField("and")
                        .invoke(expr.getRight().accept(this));
            case OROR:
                return expr.getLeft().accept(this).getField("or")
                        .invoke(expr.getRight().accept(this));
            case EQ:
                if (expr.getLeft() instanceof NameExpr) {
                    String name = ((NameExpr) expr.getLeft()).getName();

                    for (Obj obj : scopes) {
                        if (obj.hasField(name)) {
                            Obj value = expr.getRight().accept(this);
                            obj.setField(name, value);
                            return value;
                        }
                    }

                    throw new WryException(new RuntimeException("Symbol not found: \"" + name + "\"."),
                            expr.getPosition());
                } else if (expr.getLeft() instanceof SelectExpr) {
                    Obj value = expr.getRight().accept(this);
                    getFieldWrapper(expr.getLeft()).setValue(value);
                    return value;
                } else {
                    throw new UnsupportedOperationException("Destructuring is not yet implemented.");
                }
            default:
                throw new IllegalArgumentException("Unknown operator: " + expr.getToken().getType());
        }
    }

    @Override
    public Obj visit(NameExpr expr) {
        for (Obj obj : scopes) {
            if (obj.hasField(expr.getName())) {
                return obj.getField(expr.getName());
            }
        }

        throw new WryException(new RuntimeException("Symbol not found: \"" +
                expr.getName() + "\"."), expr.getPosition());
    }

    @Override
    public Obj visit(NumberExpr expr) {
        return new NumberObj(this, expr.getValue());
    }

    @Override
    public Obj visit(PrefixExpr expr) {
        switch (expr.getToken().getType()) {
            case PLUS:
                return expr.getOperand().accept(this).getField("plus").invoke();
            case MINUS:
                return expr.getOperand().accept(this).getField("minus").invoke();
            case NOT:
                return expr.getOperand().accept(this).getField("not").invoke();
            default:
                throw new IllegalArgumentException("Unknown prefix operator!");
        }
    }

    @Override
    public Obj visit(VarExpr expr) {
        Obj scope;
        String name;
        if (expr.getName() instanceof NameExpr) {
            scope = scopes.peek();
            name = ((NameExpr) expr.getName()).getName();
        } else if (expr.getName() instanceof SelectExpr) {
            scope = getFieldWrapper(((SelectExpr) expr.getName()).getLeft()).getValue();
            name = ((SelectExpr) expr.getName()).getRight().getName();
        } else {
            throw new IllegalArgumentException("Invalid variable left side.");
        }

        if (scope.hasField(name)) {
            throw new WryException(new RuntimeException("Redefinition of existing symbol \"" +
                    expr.getName() + "\"."), expr.getPosition());
        }

        scope.addField(name, expr.getDefaultValue().accept(this), expr.isMutable());
        return Obj.NULL(name);
    }

    private Obj.ObjField getFieldWrapper(Expr expr) {
        if (expr instanceof SelectExpr) {
            SelectExpr select = (SelectExpr) expr;
            Obj left = select.getLeft().accept(this);
            String right = select.getRight().getName();

            if (!left.hasField(right)) {
                throw new WryException(new RuntimeException("Symbol not found: \"" + right + "\"."), expr.getPosition());
            }

            return left.getFieldWrapper(right);
        } else if (expr instanceof NameExpr) {
            NameExpr name = (NameExpr) expr;
            String right = name.getName();

            for (Obj obj : scopes) {
                if (obj.hasField(right)) {
                    return obj.getFieldWrapper(right);
                }
            }

            throw new WryException(new RuntimeException("Symbol not found: \"" +
                    name.getName() + "\"."), expr.getPosition());
        } else {
            throw new IllegalArgumentException();
        }
    }

    Obj.ObjField getInScope(String right) {
        for (Obj obj : scopes) {
            if (obj.hasField(right)) {
                return obj.getFieldWrapper(right);
            }
        }

        throw new WryException(new RuntimeException("Symbol not found: \"" +
                right + "\"."), Position.NONE);
    }

    ClassObj findClass(String name) {
        for (Obj obj : scopes) {
            if (obj.hasField(name)) {
                return (ClassObj) obj.getField(name);
            }
        }

        throw new WryException(new RuntimeException("Class not found: \"" +
                name + "\"."), Position.NONE);
    }

    @Override
    public Obj visit(SelectExpr expr) {
        return getFieldWrapper(expr).getValue();
    }

    @Override
    public Obj visit(DefExpr expr) {
        Obj scope;
        String name;
        if (expr.getName() instanceof NameExpr) {
            scope = scopes.peek();
            name = ((NameExpr) expr.getName()).getName();
        } else if (expr.getName() instanceof SelectExpr) {
            scope = getFieldWrapper(((SelectExpr) expr.getName()).getLeft()).getValue();
            name = ((SelectExpr) expr.getName()).getRight().getName();
        } else {
            throw new IllegalArgumentException("Invalid def left side.");
        }

        if (scope.hasField(name)) {
            throw new WryException(new RuntimeException("Redefinition of existing symbol \"" +
                    expr.getName() + "\"."), expr.getPosition());
        }

        scope.addField(name, new LambdaExpr(expr.getPosition(), expr.getParams(),
                expr.getBody()).accept(this), false);

        return Obj.NULL();
    }

    @Override
    public Obj visit(BlockExpr expr) {
        Obj result = Obj.NULL();

        scopes.push(new Obj(null));
        for (Expr line : expr.getBody()) {
            result = line.accept(this);
        }
        scopes.pop();

        return result;
    }

    @Override
    public Obj visit(TupleExpr expr) {
        if (expr.getItems().size() == 1) {
            return expr.getItems().get(0).accept(this);
        } else {
            List<Obj> items = new ArrayList<>();
            for (Expr item : expr.getItems()) {
                items.add(item.accept(this));
            }
            return new TupleObj(this, items);
        }
    }

    @Override
    public Obj visit(LambdaExpr expr) {
        return new LambdaObj(args -> {
            scopes.push(new Obj(null));
            for (int i = 0; i < args.length; i++) {
                if (i > expr.getParams().size()) {
                    throw new IllegalArgumentException("Too many arguments to function!");
                }

                scopes.peek().addField(expr.getParams().get(i), args[i], false);
            }

            Obj result = expr.getBody().accept(Scope.this);
            scopes.pop();
            return result;
        });
    }

    @Override
    public Obj visit(CallExpr expr) {
        List<Expr> exprArgs = expr.getArgs();

        Obj[] args = new Obj[exprArgs.size()];
        for (int i = 0; i < exprArgs.size(); i++) {
            Expr arg = exprArgs.get(i);
            args[i] = arg.accept(this);
        }

        Obj lambda = expr.getLeft().accept(this);
        return lambda.invoke(args);
    }

    @Override
    public Obj visit(BooleanExpr expr) {
        return new BooleanObj(this, expr.getValue());
    }

    @Override
    public Obj visit(NullExpr expr) {
        return Obj.NULL();
    }

    @Override
    public Obj visit(StringExpr expr) {
        return new StringObj(this, expr.getValue());
    }

    @Override
    public Obj visit(GetExpr expr) {
        List<Expr> items = expr.getItems();
        Obj[] itemArray = new Obj[items.size()];

        for (int i = 0; i < items.size(); i++) {
            Expr item = items.get(i);
            itemArray[i] = item.accept(this);
        }

        expr.getLeft().accept(this).getField("get").invoke(itemArray);
        return null;
    }

    @Override
    public Obj visit(RecordExpr expr) {
        Obj record = new Obj(null);
        for (Map.Entry<String, Expr> entry : expr.getFields().entrySet()) {
            record.addField(entry.getKey(), entry.getValue().accept(this), true);
        }
        return record;
    }

    @Override
    public Obj visit(ClassExpr expr) {
        scopes.peek().addField(expr.getName(), new ClassObj(this, expr) {
            @Override
            public Obj invoke(Obj... args) {
                Obj instance = newInstance();
                instance.getField("init").invoke(args);
                return instance;
            }
        }, false);
        return Obj.NULL();
    }
}
