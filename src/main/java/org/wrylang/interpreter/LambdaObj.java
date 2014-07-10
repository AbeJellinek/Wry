package org.wrylang.interpreter;

public abstract class LambdaObj extends Obj {
    @Override
    public abstract Obj invoke(Obj... args);
}
