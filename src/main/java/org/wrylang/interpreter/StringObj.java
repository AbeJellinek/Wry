package org.wrylang.interpreter;

import com.google.common.collect.ImmutableMap;

public class StringObj extends Obj {
    private final String value;
    private ImmutableMap<String, Obj.ObjField> fields = ImmutableMap.<String, Obj.ObjField>builder().
            put("plus", new Obj.ObjField(new Lambda(args -> {
                checkArity(args, 1);
                Obj that = args[0];
                return new StringObj(this.getValue() + that);
            }), false)).
            put("slice", new Obj.ObjField(new Lambda(args -> {
                checkArity(args, 1, 2);
                NumberObj startIndex = (NumberObj) args[0];

                if (args.length > 1) {
                    NumberObj endIndex = (NumberObj) args[1];
                    return new StringObj(this.getValue().substring(startIndex.getValue(), endIndex.getValue()));
                } else {
                    return new StringObj(this.getValue().substring(startIndex.getValue()));
                }
            }), false)).build();

    public StringObj(String value) {
        this.value = value;
    }

    public String getValue() {
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
        return obj != null && obj instanceof StringObj &&
                ((StringObj) obj).value.equals(this.value);
    }
}
