package org.wrylang.interpreter;

public class StringObj extends Obj {
    private final String value;

    public StringObj(Scope scope, String value) {
        super(scope.findClass("String"));
        this.value = value;

        fields.put("slice", new Obj.ObjField(new LambdaObj(args -> {
            checkArity(args, 1, 2);
            NumberObj startIndex = (NumberObj) args[0];

            if (args.length > 1) {
                NumberObj endIndex = (NumberObj) args[1];
                return new StringObj(scope, this.getValue().substring(startIndex.intValue(), endIndex.intValue()));
            } else {
                return new StringObj(scope, this.getValue().substring(startIndex.intValue()));
            }
        }), false));
    }

    public String getValue() {
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
        return obj != null && obj instanceof StringObj &&
                ((StringObj) obj).value.equals(this.value);
    }
}
