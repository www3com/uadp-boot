/*******************************************************************************
 * @(#)DataAutoConfiguration.java 2018年10月02日 22:55
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.upbos.data.props.DaoConfigProps;
import com.upbos.data.exception.DataSourceNoExistException;
import com.upbos.data.core.impl.SingleDaoImpl;
import com.upbos.data.mutildatasource.MultiRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.*;

/**
 * <b>Application name：</b> DataAutoConfiguration.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2019年08月25日 22:55 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V5.0.0 <br>
 */

@Configuration
@EnableConfigurationProperties(value = DaoConfigProps.class)
@ConditionalOnClass({SingleDaoImpl.class})
public class DataSourceAutoConfiguration {

    @Autowired
    private DaoConfigProps daoConfigProps;

    /**
     * 配置数据源
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        if (daoConfigProps.getDataSource() != null) {
            DruidDataSource druidDataSource = new DruidDataSource();
            druidDataSource.configFromPropety(daoConfigProps.getDataSource());
            return druidDataSource;
        } else if (daoConfigProps.getDataSources() != null) {
            MultiRoutingDataSource multiDataSource = new MultiRoutingDataSource();
            Map<Object, Object> dataSources = new HashMap<>(16);
            String defaultSourceKey = null;
            for (String key : daoConfigProps.getDataSources().keySet()) {
                if (defaultSourceKey == null) {
                    defaultSourceKey = key;
                }
                DruidDataSource druidDataSource = new DruidDataSource();
                druidDataSource.configFromPropety(daoConfigProps.getDataSources().get(key));
                dataSources.put(key, druidDataSource);
            }

            multiDataSource.setDefaultTargetDataSource(dataSources.get(defaultSourceKey));
            multiDataSource.setTargetDataSources(dataSources);
            return multiDataSource;
        } else {
            throw new DataSourceNoExistException("配置项[dataSource][dataSources]至少有一个");
        }
    }
}