package org.wrylang.interpreter;

public class NumberObj extends Obj {
    private final int value;

    public NumberObj(Scope scope, int value) {
        super(scope.findClass("Number"));

        this.value = value;

        fields.put("plus", new ObjField(new LambdaObj(args -> {
            checkArity(args, 1);
            NumberObj that = (NumberObj) args[0];
            return new NumberObj(scope, this.getValue() + that.getValue());
        }), false));
        fields.put("minus", new ObjField(new LambdaObj(args -> {
            checkArity(args, 1);
            NumberObj that = (NumberObj) args[0];
            return new NumberObj(scope, this.getValue() - that.getValue());
        }), false));
        fields.put("times", new ObjField(new LambdaObj(args -> {
            checkArity(args, 1);
            NumberObj that = (NumberObj) args[0];
            return new NumberObj(scope, this.getValue() * that.getValue());
        }), false));
        fields.put("divide", new ObjField(new LambdaObj(args -> {
            checkArity(args, 1);
            NumberObj that = (NumberObj) args[0];
            return new NumberObj(scope, this.getValue() / that.getValue());
        }), false));
    }

    public int getValue() {
        return value;
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
