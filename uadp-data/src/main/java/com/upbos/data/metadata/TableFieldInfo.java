/*******************************************************************************
 * @(#)ColumnRelation.java 2018年09月27日 01:48
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.metadata;

import lombok.Data;

import java.util.Objects;

/**
 * <p>Title: TableFieldInfo.java</p>
 * <p>Description: 表字段和实体类属性映射关系</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
@Data
public class TableFieldInfo {
    /**
     * 字段名
     */
    private String columnName;

    /**
     * 属性名
     */
    private String fieldName;

    /**
     * 是否可以为null
     */
    private boolean nullable;

    /**
     * 是否可以插入
     */
    private boolean insertable;

    /**
     * 是否可以更新
     */
    private boolean updatable;

    /**
     * 是否可以查询
     */
    private boolean selectable;

    /**
     * 字段长度
     */
    private int length;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableFieldInfo tableFieldInfo = (TableFieldInfo) o;
        return this.columnName.equals(tableFieldInfo.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, fieldName, nullable, insertable, updatable, selectable, length);
    }
}