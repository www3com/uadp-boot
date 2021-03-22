/*
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.upbos.data.plugins.pagination.dialects;

/**
 * <p>Title: OracleDialect.java</p>
 * <p>Description: ORACLE 数据库分页语句组装实现</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class OracleDialect implements IDialect {

    @Override
    public String buildPaginationSql(String originalSql, long offset, long limit) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( ");
        sql.append(originalSql).append(" ) TMP WHERE ROWNUM <=").append((offset >= 1) ? (offset + limit) : limit);
        sql.append(") WHERE ROW_ID > ").append(offset);
        return sql.toString();
    }
}
