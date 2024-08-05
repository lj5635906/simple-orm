package com.summary.simple.orm.session;

import com.summary.simple.orm.mapping.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC执行器
 *
 * @author jie.luo
 * @since 2024/8/5
 */
public interface Executor {

    /**
     * 处理 JDBC 操作具体逻辑
     *
     * @param configuration   Configuration
     * @param mappedStatement MappedStatement
     * @param params          参数
     * @param <E>             返回值泛型
     * @return List<E>
     */
    <E> List<E> doQuery(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException;

    /**
     * 更新具体逻辑
     *
     * @param configuration   Configuration
     * @param mappedStatement MappedStatement
     * @param params          参数
     * @return 影响行数
     */
    int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;
}
