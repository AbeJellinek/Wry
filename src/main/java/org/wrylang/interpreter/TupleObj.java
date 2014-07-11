package org.wrylang.interpreter;

import com.google.common.base.Joiner;

import java.util.List;

public class TupleObj extends Obj {
    private List<Obj> items;

    public TupleObj(Scope scope, List<Obj> items) {
        super(scope.findClass("Tuple"));

        this.items = items;

        fields.put("get", new ObjField(new LambdaObj(args -> {
            checkArity(args, 1);
            return items.get(((NumberObj) args[0]).getValue());
        }), false));
    }

    @Override
    public ObjField addField(String name, Obj value, boolean mutable) {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof TupleObj && ((TupleObj) obj).getItems().equals(getItems());
    }

    @Override
    public String toString() {
        return "(" + Joiner.on(", ").join(items) + ")";
    }

    public List<Obj> getItems() {
        return items;
    }
}
