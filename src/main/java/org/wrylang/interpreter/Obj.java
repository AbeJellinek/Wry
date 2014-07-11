package org.wrylang.interpreter;

import com.google.common.base.Joiner;
import org.wrylang.parser.Position;

import java.util.HashMap;
import java.util.Map;

public class Obj {
    private ClassObj classObj;
    private Obj superInstance;

    public ClassObj getClassObj() {
        return classObj;
    }

    public static class ObjField {
        private Obj value;
        private boolean mutable;

        public ObjField(Obj value, boolean mutable) {
            this.value = value;
            this.mutable = mutable;
        }

        public Obj getValue() {
            return value;
        }

        public void setValue(Obj value) {
            if (!mutable) {
                throw new WryException(new UnsupportedOperationException("Immutable field cannot be reassigned."),
                        Position.NONE);
            }

            this.value = value;
        }

        public boolean isMutable() {
            return mutable;
        }

        public void setMutable(boolean mutable) {
            this.mutable = mutable;
        }
    }

    public static Obj NULL(String name) {
        class NullObj extends Obj {
            public NullObj() {
                super(null);
            }

            private NullPointerException ex = new NullPointerException(name);

            private WryException error() {
                return new WryException(ex, Position.NONE);
            }

            @Override
            public boolean hasField(String name) {
                throw error();
            }

            @Override
            public Obj getField(String name) {
                throw error();
            }

            @Override
            public ObjField getFieldWrapper(String name) {
                throw error();
            }

            @Override
            public ObjField addField(String name, Obj value, boolean mutable) {
                throw error();
            }

            @Override
            public void setField(String name, Obj value) {
                throw error();
            }

            @Override
            public Obj getElement(int index) {
                throw error();
            }

            @Override
            public void setElement(int index, Obj value) {
                throw error();
            }

            @Override
            public Obj invoke(Obj... args) {
                throw error();
            }

            @Override
            public String toString() {
                return "null";
            }

            @Override
            public boolean equals(Object obj) {
                return obj != null && obj instanceof NullObj;
            }
        }

        return new NullObj();
    }

    public Obj(ClassObj classObj) {
        this(classObj, null);
    }

    public Obj(ClassObj classObj, Obj superInstance) {
        this.classObj = classObj;
        this.superInstance = superInstance;
    }

    public static Obj NULL() {
        return NULL("");
    }

    protected Map<String, ObjField> fields = new HashMap<>();

    public boolean hasField(String name) {
        return (classObj != null && classObj.getMethods().containsKey(name)) || fields.containsKey(name);
    }

    public ObjField getFieldWrapper(String name) {
        if (classObj != null && classObj.getMethods().containsKey(name)) {
            return new ObjField(classObj.classMethod(this, superInstance, name), false);
        }

        return fields.get(name);
    }

    public Obj getField(String name) {
        return getFieldWrapper(name).getValue();
    }

    public ObjField addField(String name, Obj value, boolean mutable) {
        ObjField field = new ObjField(value, mutable);
        fields.put(name, field);
        return field;
    }

    public void setField(String name, Obj value) {
        getFieldWrapper(name).setValue(value);
    }

    public Obj getElement(int index) {
        throw new WryException(new RuntimeException("Object does not support indexing."), Position.NONE);
    }

    public void setElement(int index, Obj value) {
        throw new WryException(new RuntimeException("Object does not support indexing."), Position.NONE);
    }

    public Obj invoke(Obj... args) {
        throw new WryException(new RuntimeException("Object does not implement invoke."), Position.NONE);
    }

    protected void checkArity(Obj[] args, int expected) {
        if (args.length != expected) {
            throw new WryException(new IllegalArgumentException("Invalid arity (expected: " +
                    expected + ", found: " + args.length + ")."), Position.NONE);
        }
    }

    protected void checkArity(Obj[] args, int expected1, int expected2) {
        if (args.length != expected1 && args.length != expected2) {
            throw new WryException(new IllegalArgumentException("Invalid arity (expected: " +
                    expected1 + " or " + expected2 + ", found: " + args.length + ")."), Position.NONE);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Obj && ((Obj) obj).fields.equals(this.fields);
    }

    @Override
    public String toString() {
        return "{ " + Joiner.on(", ").withKeyValueSeparator(": ").join(fields) + " }";
    }
}
