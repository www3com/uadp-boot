package com.upbos.data.core.impl;

import com.upbos.data.core.XmlDao;
import com.upbos.data.core.Pagination;
import com.upbos.data.plugins.pagination.rowbounds.PaginationRowBounds;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: XmlDaoImpl.java</p>
 * <p>Description: mybatis数据持久层实现</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class XmlDaoImpl implements XmlDao {

    /**
     * mybatis manager template
     */
    SqlSessionTemplate sqlTpl;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlTpl) {
        this.sqlTpl = sqlTpl;
    }

    @Override
    public int insert(String sqlId) {
        return sqlTpl.insert(sqlId);
    }

    @Override
    public int insert(String sqlId, Object parameter) {
        return sqlTpl.insert(sqlId, parameter);
    }

    @Override
    public int batchInsert(String sqlId, List parameter) {
        return batchInsert(sqlId, parameter, 0);
    }

    @Override
    public int batchInsert(String sqlId, List parameter, int batchSize) {
        if (0 == batchSize) {
            batchSize = 500;
        }
        int returnValue = 0, total = parameter.size();

        if (total <= batchSize) {
            return sqlTpl.insert(sqlId, parameter);
        }

        int loop = (int) Math.ceil(total / (double) batchSize);
        List tempParameter = new ArrayList<>();
        int start, stop;
        for (int i = 0; i < loop; i++) {
            tempParameter.clear();
            start = i * batchSize;
            stop = Math.min(i * batchSize + batchSize - 1, total - 1);
            for (int j = start; j <= stop; j++) {
                tempParameter.add(parameter.get(j));
            }
            returnValue += sqlTpl.insert(sqlId, tempParameter);
        }
        return returnValue;
    }

    @Override
    public int update(String sqlId) {
        return sqlTpl.update(sqlId);
    }

    @Override
    public int update(String sqlId, Object parameter) {
        return sqlTpl.update(sqlId, parameter);
    }

    @Override
    public int delete(String sqlId) {
        return sqlTpl.delete(sqlId);
    }

    @Override
    public int delete(String sqlId, Object parameter) {
        return sqlTpl.delete(sqlId, parameter);
    }

    @Override
    public <T> List<T> queryList(String sqlId) {
        return sqlTpl.selectList(sqlId);
    }

    @Override
    public <T> List<T> queryList(String sqlId, Object parameter) {
        return sqlTpl.selectList(sqlId, parameter);
    }

    @Override
    public <T> Pagination<T> queryPagination(int pageNo, int pageSize, String sqlId) {
        return queryPagination(pageNo, pageSize, sqlId, null);
    }

    @Override
    public <T> Pagination<T> queryPaginationWithTotal(int pageNo, int pageSize, String sqlId) {
        return queryPaginationExt(pageNo, pageSize, sqlId, null, true);
    }

    @Override
    public <T> Pagination<T> queryPaginationWithTotal(int pageNo, int pageSize, String sqlId, Object parameter) {
        return queryPaginationExt(pageNo, pageSize, sqlId, parameter, true);
    }

    @Override
    public <T> Pagination<T> queryPagination(int pageNo, int pageSize, String sqlId, Object parameter) {
        return queryPaginationExt(pageNo, pageSize, sqlId, parameter, false);
    }

    private <T> Pagination<T> queryPaginationExt(int pageNo, int pageSize, String sqlId, Object parameter, boolean hasTotal) {
        Pagination<T> p = new Pagination<>();
        p.setPageNo(pageNo);
        p.setPageSize(pageSize);
        if (pageNo > 0) {
            pageNo = pageNo - 1;
        }
        PaginationRowBounds prd = new PaginationRowBounds(pageNo * pageSize, pageSize);
        prd.setPagination(p);
        prd.setHasTotal(hasTotal);

        p.setRows(sqlTpl.selectList(sqlId, parameter, prd));

        return p;
    }
    @Override
    public <K, V> Map<K, V> queryMap(String sqlId) {
        return sqlTpl.selectOne(sqlId);
    }

    @Override
    public <K, V> Map<K, V> queryMap(String sqlId, Object parameter) {
        return sqlTpl.selectOne(sqlId, parameter);
    }

    @Override
    public <T> T queryOne(String sqlId) {
        return sqlTpl.selectOne(sqlId);
    }

    @Override
    public <T> T queryOne(String sqlId, Object parameter) {
        return sqlTpl.selectOne(sqlId, parameter);
    }

}
