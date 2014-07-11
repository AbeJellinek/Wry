package org.wrylang.interpreter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class IntrinsicGenerator {
    public Obj generate(Scope scope, Object object) {
        Obj obj = new Obj(null);
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(Intrinsic.class)) {
                try {
                    MethodHandle handle = MethodHandles.lookup().unreflect(method).bindTo(object);
                    Intrinsic intrinsic = method.getAnnotation(Intrinsic.class);
                    String name = intrinsic.value();
                    String receiver = intrinsic.target();

                    if (receiver.isEmpty()) {
                        LambdaObj lambda = new LambdaObj(args -> {
                            try {
                                return (Obj) handle.invokeWithArguments((Object[]) args);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                return Obj.NULL();
                            }
                        });

                        if (obj.hasField(name)) {
                            obj.setField(name, lambda);
                        } else {
                            obj.addField(name, lambda, false);
                        }
                    } else {
                        ClassObj recObj = scope.findClass(receiver);
                        recObj.getMethods().put(name, (self, superObj, args) -> {
                            try {
                                return (Obj) MethodHandles.insertArguments(handle, 0, self, superObj).
                                        invokeWithArguments((Object[]) args);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                                return Obj.NULL();
                            }
                        });
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
