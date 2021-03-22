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


import com.upbos.data.util.StringPool;

/**
 * <p>Title: HSQLDialect.java</p>
 * <p>Description: HSQL 数据库分页语句组装实现</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class HsqlDialect implements IDialect {


    @Override
    public String buildPaginationSql(String originalSql, long offset, long limit) {
        StringBuilder sql = new StringBuilder(originalSql);
        sql.append(" limit ").append(offset).append(StringPool.COMMA).append(limit);
        return sql.toString();
    }
}
