package com.upbos.data.mapper;

import com.upbos.data.core.SingleDao;
import com.upbos.data.core.SpringContext;
import com.upbos.data.mapper.annotations.Insert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class InvokeInsertMethodStrategy implements InvokeMethodStrategy {
    @Override
    public Object invoke(Method method, Object[] args, Annotation annotation) {
        SingleDao singleDao = SpringContext.getBean(SingleDao.class);
        Insert insert = (Insert) annotation;
        if (insert.isBatch()) {
            return singleDao.batchInsert((List<? extends Object>) args[0], insert.ignoreProps());
        } else {
            return singleDao.insert(args[0], insert.ignoreProps());
        }
    }
}
