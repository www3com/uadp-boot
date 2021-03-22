package com.upbos.data.core.impl;

import com.upbos.data.core.Constants;
import com.upbos.data.core.SingleDao;
import com.upbos.data.persistence.annotation.IdType;
import com.upbos.data.metadata.TableInfo;
import com.upbos.data.persistence.SqlMapper;
import com.upbos.data.persistence.Where;
import com.upbos.data.util.TableInfoUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;

import static com.upbos.data.persistence.SqlParser.*;

/**
 * <p>Title: SingleDaoImpl.java</p>
 * <p>Description: 基于实体模型映射的实现</p>
 * <p>Copyright: Copyright (c) 2010-2020</p>
 * <p>Company: yideb.com</p>
 *
 * @author wangjz
 * @version 5.0.0
 * @since 2018年9月28日
 */
public class SingleDaoImpl extends XmlDaoImpl implements SingleDao {

    private SqlMapper sqlMapper;

    public SingleDaoImpl() {
        super();
    }

    public SingleDaoImpl(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlTpl = sqlSessionTemplate;
        this.sqlMapper = new SqlMapper(sqlTpl);
    }


    @Override
    public int insert(Object entity) {
        return insert(entity, null);
    }

    @Override
    public int insert(Object entity, String[] ignoreProps) {
        // 表映射信息
        TableInfo tableInfo = TableInfoUtils.getTableInfo(entity.getClass());

        String dialect = sqlTpl.getConfiguration().getVariables().getProperty(Constants.DB_DIALECT);
        if (!Constants.DB_DIALECT_MYSQL.equals(dialect.toLowerCase()) && tableInfo.getIdType() == IdType.AUTO) {
            throw new RuntimeException("不支持数据库类型：" + dialect);
        }

        int row = sqlMapper.insert(getInsertSql(entity, ignoreProps), entity);
        if (tableInfo.getIdType() == IdType.AUTO) {
            Object id = new SqlMapper(sqlTpl).selectOne("select LAST_INSERT_ID() " + tableInfo.getIdColumnName(), entity.getClass());

            PropertyDescriptor targetPd = BeanUtils.getPropertyDescriptor(entity.getClass(), tableInfo.getIdFieldName());
            assert targetPd != null;
            Method writeMethod = targetPd.getWriteMethod();

            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(id.getClass(), tableInfo.getIdFieldName());
            assert sourcePd != null;
            Method readMethod = sourcePd.getReadMethod();
            try {
                Object value = readMethod.invoke(id);
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }

                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }

                writeMethod.invoke(entity, value);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        return row;
    }


    @Override
    public <T> int batchInsert(List<T> entityList, String... ignoreProps) {
        if (entityList == null || entityList.size() == 0) {
            return 0;
        }
        String dialect = sqlTpl.getConfiguration().getVariables().getProperty("dialect");
        if (!Constants.DB_DIALECT_MYSQL.equals(dialect.toLowerCase())) {
            throw new RuntimeException("不支持数据库类型：" + dialect);
        }
        return sqlMapper.insert(getBatchInsertSql(entityList), entityList);
    }

    @Override
    public <T> int batchInsert(List<T> entityList) {
        return batchInsert(entityList, (String[]) null);
    }

    @Override
    public int updateById(Object entity) {
        return updateById(entity, (String) null);
    }

    @Override
    public int updateById(Object entity, String... ignoreProps) {
        return sqlMapper.update(getUpdateByIdSql(entity, ignoreProps), entity);
    }

    @Override
    public int update(Object entity, Where where) {
        return update(entity, where, (String) null);
    }

    @Override
    public int update(Object entity, Where where, String... ignoreProps) {
        return sqlMapper.update(getUpdateSql(entity, where, ignoreProps), entity);
    }

    @Override
    public int delete(Object entity, Where where) {
        return sqlMapper.delete(getDeleteSql(entity, where), entity);
    }

    @Override
    public int deleteById(Object entity) {
        return sqlMapper.delete(getDeleteByIdBySql(entity), entity);
    }

    @Override
    public int batchDeleteById(Collection<?> ids, Class cls) {
        return sqlMapper.delete(getBatchDeleteByIdBySql(cls), ids);
    }
}
