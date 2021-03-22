package com.upbos.data.mapper;

import com.upbos.data.mapper.annotations.Update;
import com.upbos.data.mapper.annotations.WhereType;
import com.upbos.data.core.SingleDao;
import com.upbos.data.core.SpringContext;
import com.upbos.data.persistence.Where;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class InvokeUpdateMethodStrategy implements InvokeMethodStrategy {
    @Override
    public Object invoke(Method method, Object[] args, Annotation annotation) {
        Update update = (Update) annotation;
        SingleDao singleDao = SpringContext.getBean(SingleDao.class);
        if (update.whereType() == WhereType.BY_ID) {
            return singleDao.updateById(args[0], update.ignoreProps());
        } else {
            return singleDao.update(args[0], new Where(update.where()), update.ignoreProps());
        }
    }
}
