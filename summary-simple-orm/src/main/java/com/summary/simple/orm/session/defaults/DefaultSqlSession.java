package com.summary.simple.orm.session.defaults;

import com.summary.simple.orm.mapping.MappedStatement;
import com.summary.simple.orm.mapping.SqlCommandType;
import com.summary.simple.orm.session.Configuration;
import com.summary.simple.orm.session.Executor;
import com.summary.simple.orm.session.SimpleExecutor;
import com.summary.simple.orm.session.SqlSession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * SqlSession 默认实现类
 *
 * @author jie.luo
 * @since 2024/8/5
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> E selectOne(String statementId, Object... params) throws SQLException, IllegalAccessException, IntrospectionException, InstantiationException, ClassNotFoundException, InvocationTargetException, NoSuchFieldException {
        List<Object> objects = selectList(statementId, params);
        if (null == objects || objects.size() == 0) {
            return null;
        }
        return (E) objects.get(0);
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws SQLException, IllegalAccessException, IntrospectionException, InstantiationException, NoSuchFieldException, InvocationTargetException, ClassNotFoundException {
        Executor executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if (null == mappedStatement) {
            throw new RuntimeException("当前 statementId : [" + statementId + "] 未找到对应的SQL语句");
        }
        return executor.doQuery(configuration, mappedStatement, params);
    }

    @Override
    public int insert(String statementId, Object... params) throws Exception {
        return update(statementId, params);
    }

    @Override
    public int update(String statementId, Object... params) throws Exception {
        Executor executor = new SimpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        if (null == mappedStatement) {
            throw new RuntimeException("当前 statementId : [" + statementId + "] 未找到对应的SQL语句");
        }
        return executor.update(configuration, mappedStatement, params);
    }

    @Override
    public int delete(String statementId, Object... params) throws Exception {
        return update(statementId, params);
    }

    @Override
    public <E> E getMapper(Class<?> clazz) {
        Object o = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{clazz}, ((proxy, method, args) -> {

            // 方法名 == 对应配置文件中id
            String methodName = method.getName();
            // 全限定类名 == 对应mapper.xml中namespace
            String name = method.getDeclaringClass().getName();
            // 唯一标识ID
            String statementId = name + '.' + methodName;

            SqlCommandType sqlCommandType = configuration.getMappedStatementMap().get(statementId).getSqlCommandType();

            switch (sqlCommandType) {
                case INSERT:
                case UPDATE:
                case DELETE:
                    return update(statementId, args);
                case SELECT:
                    // 获取方法返回值类型
                    Type genericReturnType = method.getGenericReturnType();
                    if (genericReturnType instanceof ParameterizedType) {
                        // 返回值包含泛型
                        return selectList(statementId, args);
                    }
                    return selectOne(statementId, args);
                case FLUSH:
                default:
                    throw new RuntimeException("当前SQL语句类型操作未定义");
            }

        }));
        return (E) o;
    }
}
