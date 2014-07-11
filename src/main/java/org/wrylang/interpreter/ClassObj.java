package org.wrylang.interpreter;

import org.wrylang.ast.ClassExpr;
import org.wrylang.ast.DefExpr;
import org.wrylang.ast.NameExpr;
import org.wrylang.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public abstract class ClassObj extends InvokableObj {
    public static interface ClassMethod {
        public Obj invoke(Obj self, Obj superObj, Obj[] args);
    }

    private Map<String, ClassMethod> methods = new HashMap<>();
    private ClassExpr expr; // bad
    private Scope scope;

    public ClassObj(Scope scope, ClassExpr expr) {
        super(null);

        this.expr = expr;
        this.scope = scope;
    }

    public Obj classMethod(Obj instance, Obj superObj, DefExpr expr) {
        return new LambdaObj(args -> {
            scope.scopes.push(new Obj(null));
            scope.scopes.peek().addField("self", instance, false);
            scope.scopes.peek().addField("super", superObj, false);
            for (int i = 0; i < args.length; i++) {
                if (i > expr.getParams().size()) {
                    throw new IllegalArgumentException("Too many arguments to function!");
                }

                scope.scopes.peek().addField(expr.getParams().get(i), args[i], false);
            }

            Obj result = expr.getBody().accept(scope);
            scope.scopes.pop();
            return result;
        });
    }

    public Obj classMethod(Obj instance, Obj superObj, String name) {
        ClassMethod method = methods.get(name);

        return new LambdaObj(args -> {
            scope.scopes.push(new Obj(null));
            Obj result = method.invoke(instance, superObj, args);
            scope.scopes.pop();
            return result;
        });
    }

    @Override
    public ObjField addField(String name, Obj value, boolean mutable) {
        methods.put(name, (self, superObj, args) -> {
            scope.scopes.push(new Obj(null));
            scope.scopes.peek().addField("self", self, false);
            scope.scopes.peek().addField("super", superObj, false);
            Obj result = value.invoke(args);
            scope.scopes.pop();
            return result;
        });
        return null;
    }

    public Obj newInstance() {
        final Obj superInstance;
        if (expr.getSuperClass() != null) {
            superInstance = ((ClassObj) scope.getInScope(expr.getSuperClass()).getValue()).newInstance();
        } else {
            superInstance = null;
        }

        Obj instance = new Obj(this, superInstance) {
            @Override
            public Obj getField(String name) {
                return super.getField(name);
            }

            @Override
            public boolean hasField(String name) {
                return getFieldWrapper(name) != null;
            }

            @Override
            public ObjField getFieldWrapper(String name) {
                if (super.hasField(name)) {
                    return super.getFieldWrapper(name);
                } else if (superInstance != null) {
                    return superInstance.getFieldWrapper(name);
                } else {
                    return null;
                }
            }
        };

        for (DefExpr def : expr.getBody()) {
            if (!(def.getName() instanceof NameExpr)) {
                throw new ParseException("Instance methods must not be extensions.", def.getPosition());
            }

            String name = ((NameExpr) def.getName()).getName();

            if (instance.hasField(name)) {
                throw new WryException(new RuntimeException("Redefinition of existing symbol \"" +
                        def.getName() + "\"."), def.getPosition());
            }

            instance.addField(name, classMethod(instance, superInstance, def), false);
        }

        return instance;
    }

    public Map<String, ClassMethod> getMethods() {
        return methods;
    }
}
