package org.wrylang.interpreter;

public class BooleanObj extends Obj {
    private final boolean value;

    public BooleanObj(boolean value) {
        this.value = value;

        fields.put("not", new Obj.ObjField(new Lambda(args -> {
            checkArity(args, 0);
            return new BooleanObj(!this.getValue());
        }), false));
        fields.put("and", new Obj.ObjField(new Lambda(args -> {
            checkArity(args, 1);
            BooleanObj that = (BooleanObj) args[0];
            return new BooleanObj(this.getValue() && that.getValue());
        }), false));
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean hasField(String name) {
        return fields.containsKey(name);
    }

    @Override
    public Obj.ObjField getFieldWrapper(String name) {
        return fields.get(name);
    }

    @Override
    public Obj.ObjField addField(String name, Obj value, boolean mutable) {
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof BooleanObj &&
                ((BooleanObj) obj).value == this.value;
    }
}
