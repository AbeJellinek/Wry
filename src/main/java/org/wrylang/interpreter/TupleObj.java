package org.wrylang.interpreter;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

import java.util.List;

public class TupleObj extends Obj {
    private List<Obj> items;
    private ImmutableMap<String, ObjField> fields = ImmutableMap.<String, ObjField>builder().
            put("get", new ObjField(new Lambda(args -> {
                checkArity(args, 1);
                return items.get(((NumberObj) args[0]).getValue());
            }), false)).build();

    public TupleObj(List<Obj> items) {
        this.items = items;
    }

    @Override
    public boolean hasField(String name) {
        return fields.containsKey(name);
    }

    @Override
    public ObjField getFieldWrapper(String name) {
        return fields.get(name);
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
