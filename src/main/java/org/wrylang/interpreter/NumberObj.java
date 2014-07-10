package org.wrylang.interpreter;

import com.google.common.collect.ImmutableMap;

public class NumberObj extends Obj {
    private final int value;
    private ImmutableMap<String, ObjField> fields = ImmutableMap.<String, ObjField>builder().
            put("plus", new ObjField(new Lambda(args -> {
                checkArity(args, 1);
                NumberObj that = (NumberObj) args[0];
                return new NumberObj(this.getValue() + that.getValue());
            }), false)).
            put("minus", new ObjField(new Lambda(args -> {
                checkArity(args, 1);
                NumberObj that = (NumberObj) args[0];
                return new NumberObj(this.getValue() - that.getValue());
            }), false)).
            put("times", new ObjField(new Lambda(args -> {
                checkArity(args, 1);
                NumberObj that = (NumberObj) args[0];
                return new NumberObj(this.getValue() * that.getValue());
            }), false)).
            put("divide", new ObjField(new Lambda(args -> {
                checkArity(args, 1);
                NumberObj that = (NumberObj) args[0];
                return new NumberObj(this.getValue() / that.getValue());
            }), false)).build();

    public NumberObj(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof NumberObj &&
                ((NumberObj) obj).getValue() == this.getValue();
    }
}
