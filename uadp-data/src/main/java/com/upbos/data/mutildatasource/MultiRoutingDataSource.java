/*******************************************************************************
 * @(#)MultiRoutingDataSource.java 2018年10月03日 10:43
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.mutildatasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * <p>Title: MultiRoutingDataSource.java</p>
 * <p>Description: 事务路由</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class MultiRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return MultiDataSourceHolder.getKey();
    }
}