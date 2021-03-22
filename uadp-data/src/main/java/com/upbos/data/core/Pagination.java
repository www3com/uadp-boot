package com.upbos.data.core;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Title: Pagination.java</p>
 * <p>Description: 分页对象，用于数据库分页</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class Pagination<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 当前页码,页码从1开始
     */
    private int pageNo;

    /**
     * 当前页行数
     */
    private int pageSize;

    /**
     * 总行数
     */
    private long total;

    public Pagination() {
        super();
    }

    public Pagination(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * 当前页的数据
     */
    @SuppressWarnings("rawtypes")
    private List<T> rows;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
