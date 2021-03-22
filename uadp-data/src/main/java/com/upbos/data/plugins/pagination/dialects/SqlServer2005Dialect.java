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

import com.upbos.data.core.Constants;
import com.upbos.data.util.StringPool;


/**
 * <p>Title: SQLServer2005Dialect.java</p>
 * <p>Description: SQLServer 2005 数据库分页方言</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
@SuppressWarnings({"ALL", "AlibabaClassNamingShouldBeCamel"})
public class SqlServer2005Dialect implements IDialect {


    private static String getOrderByPart(String sql) {
        String loweredString = sql.toLowerCase();
        int orderByIndex = loweredString.indexOf("order by");
        if (orderByIndex != -1) {
            return sql.substring(orderByIndex);
        } else {
            return StringPool.EMPTY;
        }
    }

    @Override
    public String buildPaginationSql(String originalSql, long offset, long limit) {
        StringBuilder pagingBuilder = new StringBuilder();
        String orderBy = getOrderByPart(originalSql);
        String distinctStr = StringPool.EMPTY;

        String loweredString = originalSql.toLowerCase();
        String sqlPartString = originalSql;
        if (loweredString.trim().startsWith(Constants.SQL_SELECT)) {
            int index = 6;
            if (loweredString.startsWith(Constants.SQL_SELECT_DISTINCT)) {
                distinctStr = "DISTINCT ";
                index = 15;
            }
            sqlPartString = sqlPartString.substring(index);
        }
        pagingBuilder.append(sqlPartString);

        // if no ORDER BY is specified use fake ORDER BY field to avoid errors
        if (orderBy == null) {
            orderBy = "ORDER BY CURRENT_TIMESTAMP";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("WITH selectTemp AS (SELECT ").append(distinctStr).append("TOP 100 PERCENT ")
            .append(" ROW_NUMBER() OVER (").append(orderBy).append(") as __row_number__, ").append(pagingBuilder)
            .append(") SELECT * FROM selectTemp WHERE __row_number__ BETWEEN ")
            //FIX#299：原因：mysql中limit 10(offset,size) 是从第10开始（不包含10）,；而这里用的BETWEEN是两边都包含，所以改为offset+1
            .append(offset + 1)
            .append(" AND ")
            .append(offset + limit).append(" ORDER BY __row_number__");
        return sql.toString();
    }
}
