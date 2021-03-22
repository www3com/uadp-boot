package com.upbos.data.mapper;

import com.upbos.data.mapper.annotations.BatchDelete;
import com.upbos.data.core.SingleDao;
import com.upbos.data.core.SpringContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

public class InvokeBatchDeleteMethodStrategy implements InvokeMethodStrategy {
    @Override
    public Object invoke(Method method, Object[] args, Annotation annotation) {
        BatchDelete batchDelete = (BatchDelete) annotation;
        SingleDao singleDao = SpringContext.getBean(SingleDao.class);
        return singleDao.batchDeleteById((Collection) args[0], batchDelete.value());
    }
}
