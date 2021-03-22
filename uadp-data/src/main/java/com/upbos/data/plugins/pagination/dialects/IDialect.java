/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
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
 * <p>Title: IDialect.java</p>
 * <p>Description: 数据库 分页语句组装接口</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public interface IDialect {

    /**
     * 数据类型
     */
    public static enum Type{
        /**
         * mariadb
         */
        MARIADB,
        /**
         * mysql
         */
        MYSQL,
        /**
         * oracle
         */
        ORACLE,
        /**
         * postgreSql
         */
        POSTGRESQL,
        /**
         * sqlServer
         */
        SQLSERVER,
        /**
         * sybase
         */
        SYBASE,
        /**
         * hsqldb
         */
        HSQLDB,
        /**
         * db2
         */
        DB2
    }

    /**
     * 组装分页语句
     *
     * @param originalSql 原始语句
     * @param offset      偏移量
     * @param limit       界限
     * @return 分页语句
     */
    String buildPaginationSql(String originalSql, long offset, long limit);
}
