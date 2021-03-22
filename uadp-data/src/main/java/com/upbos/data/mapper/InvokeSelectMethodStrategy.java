package com.upbos.data.mapper;

import com.upbos.data.core.Pagination;
import com.upbos.data.core.SingleDao;
import com.upbos.data.core.SpringContext;
import com.upbos.data.mapper.annotations.Select;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class InvokeSelectMethodStrategy implements InvokeMethodStrategy {
    @Override
    public Object invoke(Method method, Object[] args, Annotation annotation) {

        Select select = (Select) annotation;
        boolean isPagination = false;
        Pagination pagination = null;
        boolean withTotal = select.withTotal();

        if (method.getParameterTypes().length == 2 && args[0].getClass() == Pagination.class) {
            isPagination = true;
        }

        if (isPagination) {
            pagination = (Pagination) args[0];
        }
        SingleDao singleDao = SpringContext.getBean(SingleDao.class);
        if (!isPagination) {
            return singleDao.queryList(select.sqlId(), args.length == 2 ? args[1] : null);
        }

        if (withTotal) {
            return singleDao.queryPaginationWithTotal(pagination.getPageNo(), pagination.getPageSize(), select.sqlId(), args.length == 2 ? args[1] : null);
        } else {
            return singleDao.queryPagination(pagination.getPageNo(), pagination.getPageSize(), select.sqlId(), args.length == 2 ? args[1] : null);
        }
    }
}
