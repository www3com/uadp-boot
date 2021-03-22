package com.upbos.data.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public interface InvokeMethodStrategy {
    Object invoke(Method method, Object[] args, Annotation annotation);
}
