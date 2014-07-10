package org.wrylang.interpreter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class IntrinsicGenerator {
    public Obj generate(Object object) {
        Obj obj = new Obj();
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Intrinsic.class)) {
                try {
                    MethodHandle handle = MethodHandles.lookup().unreflect(method).bindTo(object);
                    obj.addField(method.getAnnotation(Intrinsic.class).value(), new Lambda(args -> {
                        try {
                            return (Obj) handle.invokeWithArguments(args);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                            return Obj.NULL();
                        }
                    }), false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
