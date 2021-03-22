/*******************************************************************************
 * @(#)TT.java 2019年08月29日 16:52
 * Copyright 2019 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.mutildatasource;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * <b>Application name：</b> MultiDataSourceChangeAspect.java <br>
 * <b>Application describing： 多事务拦截器, 保证该AOP在@Transactional之前执行</b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>@Date：</b> 2019年08月29日 16:52 <br>
 * <b>@author：</b> <a href="mailto:Jason@miyzh.com"> Jason </a> <br>
 * <b>@version：</b>V5.0.0 <br>
 */
@Aspect
@Component
@Order(-100)
public class MultiDataSourceChangeAspect {


    @Before("@within(ds) ")
    public void changeClassDataSource(JoinPoint joinPoint, DS ds) {
        if (existDsAnnotation(joinPoint)) {
            return;
        }
        String dsName = ds.value();
        if (dsName != null && !"".equals(dsName)) {
            MultiDataSourceHolder.setKey(dsName);
        }
    }

    @After("@within(ds)")
    public void restoreClassDataSource(JoinPoint joinPoint, DS ds) {
        if (existDsAnnotation(joinPoint)) {
            return;
        }
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        MultiDataSourceHolder.clearKey();
    }

    @Before("@annotation(ds)")
    public void changeMethodDataSource(JoinPoint joinPoint, DS ds) {
        String dsName = ds.value();
        if (dsName != null && !"".equals(dsName)) {
            MultiDataSourceHolder.setKey(dsName);
        }
    }

    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint joinPoint, DS ds) {
        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
        MultiDataSourceHolder.clearKey();
    }

    private boolean existDsAnnotation(JoinPoint joinPoint) {
        MethodSignature joinPointObject = (MethodSignature) joinPoint.getSignature();
        Method method = joinPointObject.getMethod();
        return method.isAnnotationPresent(DS.class);
    }
}