/*******************************************************************************
 * @(#)DaoConfigProperties.java 2018年10月03日 09:51
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.util.Map;
import java.util.Properties;

/**
 * <b>Application name：</b> DaoConfigProperties.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2018 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2018年10月03日 09:51 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V4.1.0 <br>
 */

@Data
@ConfigurationProperties(prefix = "dao")
public class DaoConfigProps {

    /**
     * druid数据源配置，当只有一个数据库配置，使用dataSource;
     */
    private Properties dataSource;

    /**
     * druid数据源配置，当有多个数据源配置，使用multiDataSource;
     */
    private Map<String, Properties> dataSources;

    private String dialect;

    private Resource configLocation;

    /**
     * mybatis扫描xml文件的路径
     */
    private String mapperLocations;

    /**
     * 注入拦截器的名称
     */
    private String[] interceptorNames;

}