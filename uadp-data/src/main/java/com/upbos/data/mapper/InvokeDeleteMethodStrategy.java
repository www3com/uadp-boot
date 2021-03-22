package com.upbos.data.mapper;

import com.upbos.data.mapper.annotations.Delete;
import com.upbos.data.mapper.annotations.WhereType;
import com.upbos.data.core.SingleDao;
import com.upbos.data.core.SpringContext;
import com.upbos.data.persistence.Where;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class InvokeDeleteMethodStrategy implements InvokeMethodStrategy {
    @Override
    public Object invoke(Method method, Object[] args, Annotation annotation) {
        Delete delete = (Delete) annotation;
        SingleDao singleDao = SpringContext.getBean(SingleDao.class);
        if (delete.whereType() == WhereType.BY_ID) {
            return singleDao.deleteById(args[0]);
        } else {
            return singleDao.delete(args[0], new Where(delete.where()));
        }
    }
}
