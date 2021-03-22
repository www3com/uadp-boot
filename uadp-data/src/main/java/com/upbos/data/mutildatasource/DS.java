/*******************************************************************************
 * @(#)DS.java 2018年10月03日 17:54
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.mutildatasource;

import java.lang.annotation.*;


/**
 * <p>Title: DS.java</p>
 * <p>Description: 事务选择注解</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DS {
    String value() default "";
}