package com.upbos.data.mapper;

import com.upbos.data.core.SingleDao;
import com.upbos.data.core.SpringContext;
import com.upbos.data.mapper.annotations.SelectOne;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class InvokeSelectOneMethodStrategy implements InvokeMethodStrategy {
    @Override
    public Object invoke(Method method, Object[] args, Annotation annotation) {
        SelectOne selectOne = (SelectOne) annotation;
        SingleDao singleDao = SpringContext.getBean(SingleDao.class);
        return singleDao.queryOne(selectOne.sqlId(), args == null || args.length == 0 ? null : args[0]);
    }
}
