package com.upbos.data.core;


import java.util.List;
import java.util.Map;

/**
 * <p>Title: XmlDao.java</p>
 * <p>Description: 利用mybatis实现数据持久层的访问数据接口</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public interface XmlDao {

    /**
     * 插入数据，例如：insert into table(p1, p2) values('1','2')
     *
     * @param sqlId 在mapper文件中定义的SQL Id
     * @return 影响的行数
     */
    public int insert(String sqlId);

    /**
     * 插入数据,例如： insert into table(p1, p2) values(#{p1},#{p2})
     *
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @return 影响的行数
     */
    public int insert(String sqlId, Object parameter);

    /**
     * 插入数据,例如： insert into table(p1, p2) values(#{p1},#{p2}),(#{p3},#{p4}),...
     *
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @return 影响的行数
     */
    public int batchInsert(String sqlId, List parameter);

    /**
     * 插入数据,例如： insert into table(p1, p2) values(#{p1},#{p2}),(#{p3},#{p4}),...
     *
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @param batchSize 每段的条数
     * @return 影响的行数
     */
    public int batchInsert(String sqlId, List parameter, int batchSize);

    /**
     * 更新数据，主要用于update的SQL语句，例如：update table set p1='1' where p2='2'
     *
     * @param sqlId 在mapper文件中定义的SQL Id
     * @return 影响的行数
     */
    public int update(String sqlId);

    /**
     * 更新数据，主要用于update的SQL语句，例如：update table set p1=#{p1} where p2=#{p2}
     *
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @return 影响的行数
     */
    public int update(String sqlId, Object parameter);


    /**
     * 删除数据，用于delete的SQL语句，例如：delete from table where p1='1'
     *
     * @param sqlId 在mapper文件中定义的SQL Id
     * @return 影响的行数
     */
    public int delete(String sqlId);

    /**
     * 删除数据，用于delete的SQL语句，例如：delete from table where p1=#{p1}
     *
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @return 影响的行数
     */
    public int delete(String sqlId, Object parameter);

    /**
     * 查询数据，例如：select p1,p2 from table
     *
     * @param sqlId 在mapper文件中定义的SQL Id
     * @param <T>   返回结果对象类型
     * @return 查询结果， list中的对象类型可以是引用类型对象或pojo对象
     */
    public <T> List<T> queryList(String sqlId);

    /**
     * 查询数据,例如：select p1,p2 from table where p1=#{p1}
     *
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @param <T>       返回结果对象类型
     * @return 查询结果， list中的对象类型可以是引用类型对象或pojo对象
     */
    public <T> List<T> queryList(String sqlId, Object parameter);

    /**
     * 分页查询数据
     *
     * @param pageNo   当前页码，从1开始
     * @param pageSize 每页条数
     * @param sqlId    在mapper文件中定义的SQL Id
     * @return 查询结果， list中的对象类型可以是引用类型对象或pojo对象
     */
    public <T> Pagination<T> queryPagination(int pageNo, int pageSize, String sqlId);

    /**
     * 分页查询数据
     *
     * @param pageNo    当前页码，从1开始
     * @param pageSize  每页条数
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @return 查询结果， list中的对象类型可以是引用类型对象或pojo对象
     */
    public <T> Pagination<T> queryPagination(int pageNo, int pageSize, String sqlId, Object parameter);

    /**
     * 分页查询数据
     *
     * @param pageNo   当前页码，从1开始
     * @param pageSize 每页条数
     * @param sqlId    在mapper文件中定义的SQL Id
     * @return 查询结果， list中的对象类型可以是引用类型对象或pojo对象
     */
    public <T> Pagination<T> queryPaginationWithTotal(int pageNo, int pageSize, String sqlId);

    /**
     * 分页查询数据
     *
     * @param pageNo    当前页码，从1开始
     * @param pageSize  每页条数
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @return 查询结果， list中的对象类型可以是引用类型对象或pojo对象
     */
    public <T> Pagination<T> queryPaginationWithTotal(int pageNo, int pageSize, String sqlId, Object parameter);

    /**
     * 查询数据，SQL语句的查询结果中只能包含一条记录
     *
     * @param sqlId 在mapper文件中定义的SQL Id
     * @param <K>   返回结果对象类型K
     * @param <V>   返回结果对象类型V
     * @return 返回一个map对象
     */
    public <K, V> Map<K, V> queryMap(String sqlId);

    /**
     * 查询数据，SQL语句的查询结果中只能包含一条记录
     *
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型或pojo对象
     * @param <K>       返回结果对象类型K
     * @param <V>       返回结果对象类型V
     * @return 返回一个Map对象
     */
    public <K, V> Map<K, V> queryMap(String sqlId, Object parameter);

    /**
     * 查询数据，SQL语句的查询结果中只能包含一条记录
     *
     * @param sqlId 在mapper文件中定义的SQL Id
     * @param <T>   返回结果对象类型
     * @return 返回一个对象，可以是引用类型对象或pojo对象
     */
    public <T> T queryOne(String sqlId);

    /**
     * 查询数据，SQL语句的查询结果中只能包含一条记录
     *
     * @param sqlId     在mapper文件中定义的SQL Id
     * @param parameter 可以是引用类型对象或pojo对象
     * @param <T>       返回结果对象类型
     * @return 返回一个对象，可以是引用类型对象或pojo对象
     */
    public <T> T queryOne(String sqlId, Object parameter);


}
