package org.wrylang.interpreter;

public class BooleanObj extends Obj {
    private final boolean value;

    public BooleanObj(Scope scope, boolean value) {
        super(scope.findClass("Boolean"));
        this.value = value;

        fields.put("and", new Obj.ObjField(new LambdaObj(args -> {
            checkArity(args, 1);
            BooleanObj that = (BooleanObj) args[0];
            return new BooleanObj(scope, this.getValue() && that.getValue());
        }), false));
    }

    public boolean getValue() {
        return value;
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
