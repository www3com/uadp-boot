/*******************************************************************************
 * @(#)WhereWrapper.java 2018年09月26日 23:28
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.persistence;


import com.upbos.data.util.StringPool;

/**
 * <p>Title: Where.java</p>
 * <p>Description: where条件拼接类，支持and和or以及字符串连接</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class Where {
    private StringBuffer whereSql;

    public Where() {
        whereSql = new StringBuffer();
    }

    public Where(String condition) {
        whereSql = new StringBuffer();
        whereSql.append(condition);
    }

    public Where add(String condition) {
        whereSql.append(StringPool.SPACE)
                .append(StringPool.AND)
                .append(StringPool.SPACE)
                .append(condition == null ? "" : condition);
        return this;
    }

    public Where or(String condition) {
        whereSql.append(StringPool.SPACE)
                .append(StringPool.OR)
                .append(StringPool.SPACE)
                .append(condition == null ? "" : condition);
        return this;
    }

    public Where concat(String condition) {
        whereSql.append(condition == null ? "" : condition);
        return this;
    }

    @Override
    public String toString() {

        StringBuffer prefixAdd = new StringBuffer().append(StringPool.SPACE).append(StringPool.AND);
        StringBuffer prefixOr = new StringBuffer().append(StringPool.SPACE).append(StringPool.OR);

        if (prefixAdd.toString().equals(whereSql.substring(0, WHERE_ADD_END_POS))) {
            return whereSql.substring(5);
        }
        if (prefixOr.toString().equals(whereSql.substring(0, WHERE_OR_END_POS))) {
            return whereSql.substring(4);
        }
        return whereSql.toString();
    }

    private static final int WHERE_ADD_END_POS = 4;

    private static final int WHERE_OR_END_POS = 3;
}