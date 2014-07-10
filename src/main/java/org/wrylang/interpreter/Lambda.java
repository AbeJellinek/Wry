package org.wrylang.interpreter;

import java.util.function.Function;

public class Lambda extends LambdaObj {
    private Function<Obj[], Obj> function;

    public Lambda(Function<Obj[], Obj> function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "<function>";
    }

    @Override
    public Obj invoke(Obj... args) {
        return function.apply(args);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj; // lambdas are stingy
    }
}
