package com.upbos.data.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MapperProxy implements InvocationHandler {
    private Class<?> interfaceClass;

    public Object bind(Class<?> cls) {
        this.interfaceClass = cls;
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{interfaceClass}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Annotation[] annotations = method.getDeclaredAnnotations();
        if (annotations == null || annotations.length == 0) {
            return null;
        }

        Object rtn = null;
        for (Annotation annotation : annotations) {
            InvokeMethodEnum invokeMethodEnum = InvokeMethodEnum.valueOf(annotation.annotationType().getSimpleName());
            if (invokeMethodEnum != null) {
                rtn = invokeMethodEnum.getInvokeMethodStrategy().invoke(method, args, annotation);
            }
        }
        return rtn;
    }
}
