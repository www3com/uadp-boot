/*******************************************************************************
 * @(#)TableInfoUtils.java 2018年09月28日 15:58
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.util;

import com.upbos.data.metadata.TableFieldInfo;
import com.upbos.data.metadata.TableInfo;
import com.upbos.data.persistence.annotation.Column;
import com.upbos.data.persistence.annotation.Id;
import com.upbos.data.persistence.annotation.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title: TableInfoUtils.java</p>
 * <p>Description: 表注解信息操作</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class TableInfoUtils {
    /**
     * 缓存反射类表信息
     */
    private static final Map<String, TableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap<>();

    public static TableInfo getTableInfo(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        // 从缓存中取出
        TableInfo tableInfo = TABLE_INFO_CACHE.get(clazz.getName());
        if (tableInfo == null) {
            tableInfo = new TableInfo();
        } else {
            return tableInfo;
        }

        for (Class<?> clz = clazz; clz != Object.class; clz = clz.getSuperclass()) {
            //获取类注解信息
            Table tableAnnotation = clz.getAnnotation(Table.class);
            if (tableAnnotation != null && tableInfo.getTableName() == null) {
                // 获取表名称
                String tableName = tableAnnotation.value();
                if ("".equals(tableName)) {
                    tableName = clz.getSimpleName();
                }
                tableInfo.setTableName(tableName);
            }

            // 表字段信息列表
            if (tableInfo.getFieldInfoList() == null) {
                tableInfo.setFieldInfoList(new ArrayList<>());
            }

            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                Annotation[] annotations = field.getAnnotations();
                // 方法上没有注解跳过
                if (annotations == null) {
                    continue;
                }

                for (Annotation annotation : annotations) {
                    if (annotation.annotationType().equals(Id.class) && tableInfo.getIdColumnName() == null) {
                        Id id = (Id) annotation;
                        tableInfo.setIdColumnName("".equals(id.value()) ? field.getName() : id.value());
                        tableInfo.setIdFieldName(field.getName());
                        tableInfo.setIdType(id.idType());
                    } else if (annotation.annotationType().equals(Column.class)) {
                        Column column = (Column) annotation;
                        TableFieldInfo tableFieldInfo = new TableFieldInfo();
                        tableFieldInfo.setColumnName("".equals(column.value()) ? field.getName() : column.value());
                        tableFieldInfo.setFieldName(field.getName());
                        tableFieldInfo.setNullable(column.nullable());
                        tableFieldInfo.setInsertable(column.insertable());
                        tableFieldInfo.setUpdatable(column.updatable());
                        tableFieldInfo.setSelectable(column.selectable());
                        if (!tableInfo.getFieldInfoList().contains(tableFieldInfo)) {
                            tableInfo.getFieldInfoList().add(tableFieldInfo);
                        }
                    }
                }
            }
        }

        if (tableInfo.getTableName() == null) {
            throw new RuntimeException(String.format("类 %s 缺少注解@Table", clazz.getName()));
        }
        TABLE_INFO_CACHE.put(clazz.getName(), tableInfo);

        return tableInfo;
    }

}