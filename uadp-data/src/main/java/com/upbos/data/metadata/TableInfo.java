/*******************************************************************************
 * @(#)TableInfo.java 2018年09月28日 15:15
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.metadata;

import com.upbos.data.persistence.annotation.IdType;

import java.util.List;

/**
 * <p>Title: TableInfo.java</p>
 * <p>Description: 表名和实体类名称映射关系</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class TableInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表主键ID 字段名
     */
    private String idColumnName;

    /**
     * 表主键ID 属性名
     */
    private String idFieldName;

    /**
     * 表主键ID 类型
     */
    private IdType idType = IdType.NONE;

    /**
     * 表字段信息列表
     */
    private List<TableFieldInfo> fieldInfoList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIdColumnName() {
        return idColumnName;
    }

    public void setIdColumnName(String idColumnName) {
        this.idColumnName = idColumnName;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public List<TableFieldInfo> getFieldInfoList() {
        return fieldInfoList;
    }

    public void setFieldInfoList(List<TableFieldInfo> fieldInfoList) {
        this.fieldInfoList = fieldInfoList;
    }
}