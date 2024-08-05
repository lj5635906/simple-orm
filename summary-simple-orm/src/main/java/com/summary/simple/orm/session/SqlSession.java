package com.summary.simple.orm.session;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jie.luo
 * @since 2024/8/5
 */
public interface SqlSession {

    /**
     * 获取单条数据
     *
     * @param statementId SQL唯一标识ID
     * @param params      参数
     * @param <E>         参数
     * @return E
     */
    <E> E selectOne(String statementId, Object... params) throws SQLException, IllegalAccessException, IntrospectionException, InstantiationException, ClassNotFoundException, InvocationTargetException, NoSuchFieldException;

    /**
     * 获取多条数据
     *
     * @param statementId SQL唯一标识ID
     * @param params      参数
     * @param <E>         返回值泛型
     * @return List<E>
     */
    <E> List<E> selectList(String statementId, Object... params) throws SQLException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException;

    /**
     * 插入方法
     *
     * @param statementId SQL唯一标识ID
     * @param params 参数
     * @return 影响行数
     */
    int insert(String statementId, Object... params) throws Exception;

    /**
     * 插入方法
     *
     * @param statementId SQL唯一标识ID
     * @param params 参数
     * @return 影响行数
     */
    int update(String statementId, Object... params) throws Exception;

    /**
     * 插入方法
     *
     * @param statementId SQL唯一标识ID
     * @param params 参数
     * @return 影响行数
     */
    int delete(String statementId, Object... params) throws Exception;

    /**
     * 根据 动态代理 方式执行 SQL
     *
     * @param clazz 代理的接口类
     * @param <E>   返回值泛型
     * @return mapper 绑定的 SqlSession
     */
    <E> E getMapper(Class<?> clazz);
}
