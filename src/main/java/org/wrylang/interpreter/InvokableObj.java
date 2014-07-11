package org.wrylang.interpreter;

public abstract class InvokableObj extends Obj {
    public InvokableObj(ClassObj classObj) {
        super(classObj);
    }

    @Override
    public abstract Obj invoke(Obj... args);
}
