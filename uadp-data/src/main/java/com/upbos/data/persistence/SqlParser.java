/*******************************************************************************
 * @(#)SqlParser.java 2018年09月27日 02:32
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.persistence;

import com.upbos.data.metadata.TableFieldInfo;
import com.upbos.data.metadata.TableInfo;
import com.upbos.data.persistence.annotation.IdType;
import com.upbos.data.util.TableInfoUtils;
import com.upbos.data.util.ReflectUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>Title: SqlParser.java</p>
 * <p>Description: 组装SQL</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class SqlParser {

    private static boolean existProp(String columnName, String[] ignoreProps) {
        if (columnName == null || ignoreProps == null || ignoreProps.length == 0) {
            return false;
        }
        for (String prop : ignoreProps) {
            if (columnName.equals(prop)) {
                return true;
            }
        }
        return false;
    }

    private static String getFormatInsertSql(Object entity, String[] ignoreProps, boolean isBatch) {
        String insertSqlPlaceholder = "INSERT INTO %s(%s) VALUES (%s)";
        String batchInsertSqlPlaceholder = "<script>INSERT INTO %s(%s) VALUES "
                + "<foreach collection=\"list\" item=\"item\"  separator=\",\">"
                + "(%s)</foreach></script> ";

        String columnPlaceholder = ", %s";
        String valuePlaceholder = " ,#{%s}";
        String itemPlaceholder = ", #{item.%s}";

        // 列名
        StringBuffer columnSection = new StringBuffer();
        // 列值占位符
        StringBuffer valueSection = new StringBuffer();

        // 表映射信息
        TableInfo tableInfo = TableInfoUtils.getTableInfo(entity.getClass());

        // 如果有主键列
        if (tableInfo.getIdColumnName() != null && tableInfo.getIdType() != IdType.AUTO) {
            columnSection.append(String.format(columnPlaceholder, tableInfo.getIdColumnName()));
            valueSection.append(String.format(isBatch ? itemPlaceholder : valuePlaceholder, tableInfo.getIdFieldName()));
        }

        Map<String, Object> map = ReflectUtils.objectToMap(entity);
        for (TableFieldInfo fieldInfo : tableInfo.getFieldInfoList()) {
            if (existProp(fieldInfo.getFieldName(), ignoreProps) || map.get(fieldInfo.getFieldName()) == null) {
                continue;
            }
            columnSection.append(String.format(columnPlaceholder, fieldInfo.getColumnName()));
            valueSection.append(String.format(isBatch ? itemPlaceholder: valuePlaceholder, fieldInfo.getFieldName()));
        }

        return String.format(isBatch ? batchInsertSqlPlaceholder : insertSqlPlaceholder, tableInfo.getTableName(),
                columnSection.substring(2),
                valueSection.substring(2));
    }

    /**
     * 获取Insert的sql语句，
     * 例如： INSERT INTO upm_org(id, name, value) VALUES (#{id}, #{name}, #{value})
     *
     * @param entity
     * @return
     */
    public static String getInsertSql(Object entity, String[] ignoreProps) {
        return getFormatInsertSql(entity, ignoreProps, false);
    }

    public static <T> String getBatchInsertSql(List<T> list, String... ignoreProps) {
        if (list == null || list.size() == 0) {
            return "";
        }

        T first = list.get(0);

        return getFormatInsertSql(first, ignoreProps, true);
    }

    private static StringBuffer getUpdateSqlSetSection(TableInfo tableInfo, Object entity, String[] ignoreProps) {
        String updateSqlPlaceholder = "UPDATE %s SET %s";
        String columnPlaceholder = " ,%s = #{%s}";
        // set部分的sql
        StringBuffer setSection = new StringBuffer();


        Map<String, Object> map = ReflectUtils.objectToMap(entity);
        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldInfoList()) {
            if (existProp(tableFieldInfo.getFieldName(), ignoreProps) ||
                    map.get(tableFieldInfo.getFieldName()) == null) {
                continue;
            }
            setSection.append(String.format(columnPlaceholder, tableFieldInfo.getColumnName(), tableFieldInfo.getFieldName()));
        }
        StringBuffer sql = new StringBuffer();
        if (setSection.length() > 0) {
            sql.append(String.format(updateSqlPlaceholder, tableInfo.getTableName(), setSection.substring(2)));
        }

        return sql;
    }

    /**
     * 获取update的SQL语句
     *
     * @param entity 实体
     * @param where  where条件
     * @return 返回sql语句
     */
    public static String getUpdateSql(Object entity, Where where, String[] ignoreProps) {
        // 表映射信息
        TableInfo tableInfo = TableInfoUtils.getTableInfo(entity.getClass());

        StringBuffer sql = getUpdateSqlSetSection(tableInfo, entity, ignoreProps);

        if (where != null) {
            sql.append(" WHERE ").append(where.toString());
        }
        return sql.toString();
    }

    /**
     * 拼接updateById的SQL语句
     *
     * @param entity 实体对象
     * @return 返回sql
     */
    public static String getUpdateByIdSql(Object entity, String[] ignoreProps) {
        // 表映射信息
        TableInfo tableInfo = TableInfoUtils.getTableInfo(entity.getClass());
        if (tableInfo.getIdColumnName() == null) {
            throw new RuntimeException(String.format("类 %s 缺失@Id", entity.getClass().getName()));
        }

        String updateSqlPlaceholder = "%s WHERE %s = #{%s}";


        StringBuffer setSectionSql = getUpdateSqlSetSection(tableInfo, entity, ignoreProps);

        StringBuffer sql = new StringBuffer();
        sql.append(String.format(updateSqlPlaceholder,
                setSectionSql,
                tableInfo.getIdColumnName(), tableInfo.getIdFieldName()));

        return sql.toString();
    }

    public static String getDeleteSql(Object entity, Where where) {
        String deleteSqlPlaceholder = "DELETE FROM %s ";

        // 表映射信息
        TableInfo tableInfo = TableInfoUtils.getTableInfo(entity.getClass());


        StringBuffer sql = new StringBuffer();
        sql.append(String.format(deleteSqlPlaceholder, tableInfo.getTableName()));

        if (where != null) {
            sql.append(" WHERE ").append(where.toString());
        }
        return sql.toString();
    }

    /**
     * 拼接deleteById的SQL语句
     *
     * @param entity
     * @return
     */
    public static String getDeleteByIdBySql(Object entity) {
        // 表映射信息
        TableInfo tableInfo = TableInfoUtils.getTableInfo(entity.getClass());
        if (tableInfo.getIdColumnName() == null) {
            throw new RuntimeException(String.format("类 %s 缺失@Id", entity.getClass().getName()));
        }

        String deleteSqlPlaceholder = "DELETE FROM %s WHERE %s = #{%s}";
        return String.format(deleteSqlPlaceholder, tableInfo.getTableName(), tableInfo.getIdColumnName(), tableInfo.getIdFieldName());
    }

    public static <T> String getBatchDeleteByIdBySql(Class<?> clazz) {

        String sqlPlaceholder = "<script>DELETE FROM %s WHERE %s in "
                + "<foreach collection=\"list\" item=\"item\" open=\"(\" separator=\",\" close=\")\">"
                + "#{item}</foreach></script> ";

        // 表映射信息
        TableInfo tableInfo = TableInfoUtils.getTableInfo(clazz);

        return String.format(sqlPlaceholder, tableInfo.getTableName(),
                tableInfo.getIdColumnName());
    }

}