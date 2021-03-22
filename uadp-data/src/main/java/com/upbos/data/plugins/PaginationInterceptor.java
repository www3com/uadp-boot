package com.upbos.data.plugins;

import com.upbos.data.core.Pagination;
import com.upbos.data.plugins.pagination.dialects.IDialect;
import com.upbos.data.plugins.pagination.dialects.MySqlDialect;
import com.upbos.data.plugins.pagination.dialects.OracleDialect;
import com.upbos.data.plugins.pagination.dialects.PostgreDialect;
import com.upbos.data.plugins.pagination.rowbounds.PaginationRowBounds;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;


/**
 * <p>Title: PaginationInterceptor.java</p>
 * <p>Description: 分页拦截器</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class PaginationInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();

        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
        IDialect.Type databaseType = null;
        try {
            databaseType = IDialect.Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());
        } catch (Exception e) {
            //ignore
        }
        if (databaseType == null) {
            throw new RuntimeException("the value of the dialect property in configuration.xml is not defined : " + configuration.getVariables().getProperty("dialect"));
        }

        RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
            return invocation.proceed();
        }

        IDialect dialect = null;
        switch (databaseType) {
            case MYSQL:
                dialect = new MySqlDialect();
                break;
            case ORACLE:
                dialect = new OracleDialect();
                break;
            case POSTGRESQL:
                dialect = new PostgreDialect();
                break;
            default:
                return invocation.proceed();
        }

        String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        metaStatementHandler.setValue("delegate.boundSql.sql", dialect.buildPaginationSql(originalSql, rowBounds.getOffset(), rowBounds.getLimit()));
        metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
        metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

        if (rowBounds instanceof PaginationRowBounds && ((PaginationRowBounds) rowBounds).isHasTotal()) {
            Connection connection = (Connection) invocation.getArgs()[0];
            //记录统计
            String countSql = "select count(0) from (" + originalSql + ")  tmp_count";
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
            PreparedStatement countStmt = connection.prepareStatement(countSql);
            BoundSql countBs = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

            // issue 分页插件使用foreach参数失败的bug 2017-10-31
            //重新赋值addtionalParameter。DefaultParameterHandler.setParameters解析sql参数时，会从addtionalParameter
            //获取其他参数值，比如mybatis的foreach 参数。这里采用new BoundSql()没有拷贝原boundSql的addtionalParameter值
            List<ParameterMapping> pmLs = boundSql.getParameterMappings();
            for (ParameterMapping pm : pmLs) {
                String propertyName = pm.getProperty();
                //这里只是增加以"_frec_"开始的参数，表示是for each参数，其他参数可以从ParameterObject获取。
                if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)) {
                    countBs.setAdditionalParameter(propertyName, boundSql.getAdditionalParameter(propertyName));
                }
            }
            // issue 分页插件使用foreach参数失败的bug 2017-10-31

            setParameters(countStmt, mappedStatement, countBs, boundSql.getParameterObject());
            ResultSet rs = countStmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            countStmt.close();
            Pagination pt = ((PaginationRowBounds) rowBounds).getPagination();
            pt.setTotal(count);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

}