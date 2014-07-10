package org.wrylang.interpreter;

public class WryIntrinsics {
    @Intrinsic("print")
    public Obj print(Obj o) {
        System.out.println(o);
        return Obj.NULL();
    }
}
