/*******************************************************************************
 * @(#)LmDao.java 2018年09月27日 11:33
 * Copyright 2018 upbos.com. All rights reserved.
 *******************************************************************************/
package com.upbos.data.core;

import com.upbos.data.persistence.Where;

import java.util.Collection;
import java.util.List;

/**
 * <p>Title: SingleDao.java</p>
 * <p>Description: 基于实体模型映射的接口</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */

public interface SingleDao extends XmlDao{

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return 影响的行数
     */
    int insert(Object entity);

    /**
     * 插入数据，并不插入忽略属性的字段
     * @param entity 数据对象
     * @param ignoreProps 忽略属性
     * @return 影响的行数
     */
    int insert(Object entity, String ...ignoreProps);

    /**
     * 批量插入数据, 并不插入忽略属性的字段
     *
     * @param list 实体对象list
     * @return 影响的行数
     */
    <T> int batchInsert(List<T> list);

    /**
     * 批量插入数据
     * @param list 数据对象list
     * @param ignoreProps 忽略属性
     * @return 影响的行数
     */
    <T> int batchInsert(List<T> list, String ...ignoreProps);

    /**
     * 根据id更新数据
     *
     * @param entity 更新的实体对象
     * @return 影响的行数
     */
    int updateById(Object entity);

    /**
     * 根据id更新数据，并不更新忽略属性的字段
     * @param entity 更新的数据对象
     * @param ignoreProps 忽略属性
     * @return 影响的行数
     */
    int updateById(Object entity, String ...ignoreProps);

    /**
     * 根据条件更新数据
     *
     * @param entity           更新的实体对象
     * @param where where条件, 例如：
     *                         Where where = new Where()
     *                         .add("name = #{name}").or("remark = #{remark}");
     * @return
     */
    int update(Object entity, Where where);

    /**
     * 根据条件更新数据，并不更新忽略属性的字段
     * @param entity 更新的数据对象
     * @param where where条件, 例如：
     *                        Where where = new Where()
     *                          .add("name = #{name}").or("remark = #{remark}");
     * @param ignoreProps 忽略属性
     * @return 影响的行数
     */
    int update(Object entity, Where where, String ...ignoreProps);

    /**
     * 根据id删除数据
     *
     * @param entity 删除的实体对象
     * @return 影响的行数
     */
    int deleteById(Object entity);

    /**
     * 根据条件删除数据
     *
     * @param entity           删除数据的实体对象
     * @param where where条件, 例如：
     *                         Where where = new Where()
     *                         .add("name = #{name}").or("remark = #{remark}");
     * @return 影响的行数
     */
    int delete(Object entity, Where where);

    /**
     * 根据id批量删除数据
     * @param ids 删除对象id列表
     * @param tClass 删除对象class
     * @return 影响的条数
     */
    int batchDeleteById(Collection<?> ids, Class tClass);
}