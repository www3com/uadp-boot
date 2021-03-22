/*******************************************************************************
 * @(#)DataAutoConfiguration.java 2018年10月02日 22:55
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.configuration;

import com.upbos.data.props.DaoConfigProps;
import com.upbos.data.core.SingleDao;
import com.upbos.data.core.impl.SingleDaoImpl;
import com.upbos.data.plugins.PaginationInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>Application name：</b> DataAutoConfiguration.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2020 upbos.com 版权所有。<br>
 * <b>Company：</b> upbos.com <br>
 * <b>Date：</b> 2019年08月25日 22:55 <br>
 * <b>author：</b> <a href="mailto:wangjz@miyzh.com"> wangjz </a> <br>
 * <b>version：</b>V5.0.0 <br>
 * @author wangjz
 */

@Configuration
@EnableConfigurationProperties(value = DaoConfigProps.class)
@ConditionalOnClass({SingleDaoImpl.class})
public class DaoAutoConfiguration {

    @Autowired
    private DaoConfigProps daoConfigProps;


    /**
     * 配置事物管理器
     *
     * @return
     */
    @Bean(name = "masterTransactionManager")
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    /**
     * 配置mybatis分页拦截器
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 配置mybatis session生成工厂
     *
     * @return
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, Interceptor[] interceptors) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        // 设置拦截器
        if (interceptors.length >= 2) {
            List<Interceptor> list = Arrays.stream(interceptors).filter(interceptor -> !(interceptor instanceof PaginationInterceptor)).collect(Collectors.toList());
            sqlSessionFactoryBean.setPlugins(list.toArray(new Interceptor[list.size()]));
        } else {
            sqlSessionFactoryBean.setPlugins(interceptors);
        }

        // 设置mybatis自定义配置项
        Properties properties = new Properties();
        properties.setProperty("dialect", daoConfigProps.getDialect() == null ? "mysql" : daoConfigProps.getDialect());
        sqlSessionFactoryBean.setConfigurationProperties(properties);

        // 设置mybatis扫描路径

        String mapperLocations = daoConfigProps.getMapperLocations();
        if (mapperLocations == null) {
            mapperLocations = "classpath*:mapper/**/*.xml";
        }
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(mapperLocations);
        sqlSessionFactoryBean.setMapperLocations(resources);
        if (daoConfigProps.getConfigLocation() != null && daoConfigProps.getConfigLocation().exists()) {
            sqlSessionFactoryBean.setConfigLocation(daoConfigProps.getConfigLocation());
        } else {
            sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:default-mybatis.xml"));
        }

        return sqlSessionFactoryBean;

    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactoryBean) {
        return new SqlSessionTemplate(sqlSessionFactoryBean);
    }

    @Bean
    @ConditionalOnMissingBean(SingleDaoImpl.class)
    public SingleDao getSingleDao(SqlSessionTemplate sqlSessionTemplate) {
        return new SingleDaoImpl(sqlSessionTemplate);
    }
}