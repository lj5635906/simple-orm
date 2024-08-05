package com.summary.simple.orm.session;

import com.summary.simple.orm.mapping.BoundSql;
import com.summary.simple.orm.mapping.MappedStatement;
import com.summary.simple.orm.mapping.ParameterMapping;
import com.summary.simple.orm.parseing.GenericTokenParser;
import com.summary.simple.orm.parseing.ParameterMappingTokenHandler;
import com.summary.simple.orm.type.SimpleTypeRegistry;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC执行器实现类
 *
 * @author jie.luo
 * @since 2024/8/5
 */
public class SimpleExecutor implements Executor {

    @Override
    public <E> List<E> doQuery(Configuration configuration, MappedStatement mappedStatement, Object... params) throws SQLException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {

        // 1.获取 JDBC 数据库连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2.解析SQL中#{}/${},使用?替换
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        System.out.println("执行SQL: " + boundSql.getSqlTest());
        // 3.获取预编译对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlTest());

        // 4.设置参数
        Class<?> parameterTypeClass = getClassType(mappedStatement.getParameterType());
        StringBuilder sb = new StringBuilder("参数: ");
        for (int i = 0; i < boundSql.getParameterMappings().size(); i++) {
            ParameterMapping parameterMapping = boundSql.getParameterMappings().get(i);
            // #{id} 中的名称 id
            String content = parameterMapping.getContent();
            // 根据反射获取对应的值
            // 获取 id 对应的字段
            Field declaredField = parameterTypeClass.getDeclaredField(content);
            // 设置为true，暴力访问
            declaredField.setAccessible(true);

            // 获取对应的值
            Object o = declaredField.get(params[0]);
            preparedStatement.setObject(i + 1, o);

            sb.append(" [" + content + " : " + o + "] ");
        }
        System.out.println(sb.toString());

        // 5.执行SQL，并获取结果集
        ResultSet resultSet = preparedStatement.executeQuery();

        // 6.封装返回结果集
        Class<?> resultTypeClass = getClassType(mappedStatement.getResultType());
        List<Object> results = new ArrayList<>();
        while (resultSet.next()) {
            Object o = resultTypeClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                // 字段名
                String columnName = metaData.getColumnName(i);
                // 获取值
                Object value = resultSet.getObject(columnName);

                // 根据内省给对象属性写入值
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            results.add(o);
        }
        return (List<E>) results;
    }

    @Override
    public int update(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {

        // 1.获取 JDBC 数据库连接
        Connection connection = configuration.getDataSource().getConnection();

        // 2.解析SQL中#{}/${},使用?替换
        String sql = mappedStatement.getSql();
        BoundSql boundSql = getBoundSql(sql);
        System.out.println("执行SQL: " + boundSql.getSqlTest());
        // 3.获取预编译对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSqlTest());

        // 4.设置参数
        Class<?> parameterTypeClass = getClassType(mappedStatement.getParameterType());
        StringBuilder sb = new StringBuilder("参数: ");
        for (int i = 0; i < boundSql.getParameterMappings().size(); i++) {
            ParameterMapping parameterMapping = boundSql.getParameterMappings().get(i);
            // #{id} 中的名称 id
            String content = parameterMapping.getContent();

            Object o = null;
            // 验证参数类型是否为基础数据类型的引用类型
            boolean isWrapClass = SimpleTypeRegistry.isSimpleType(parameterTypeClass);
            if (isWrapClass) {
                o = params[0];
                preparedStatement.setObject(i + 1, o);
            } else {
                // 根据反射获取对应的值
                // 获取 id 对应的字段
                Field declaredField = parameterTypeClass.getDeclaredField(content);
                // 设置为true，暴力访问
                declaredField.setAccessible(true);

                // 获取对应的值
                o = declaredField.get(params[0]);
                preparedStatement.setObject(i + 1, o);
            }

            sb.append(" [" + content + " : " + o + "] ");
        }
        System.out.println(sb.toString());

        return preparedStatement.executeUpdate();
    }

    /**
     * 根据全限定类名加载类
     *
     * @param classResource 全限定类名
     * @return Class<?>
     * @throws ClassNotFoundException
     */
    private Class<?> getClassType(String classResource) throws ClassNotFoundException {
        if (null != classResource) {
            return Class.forName(classResource);
        }
        return null;
    }

    /**
     * 完成对 #{} 的解析工作，将 #{} 使用 ? 进行代替
     * 解析出 #{} 里面的值进行存储
     *
     * @param sql SQL语句 : select * from USERS where USER_ID = #{userId} and USER_NAME = #{userName}
     * @return BoundSql
     */
    private BoundSql getBoundSql(String sql) {

        // 标记处理类：配置标记解析器来完成对占位符的解析处理工作
        ParameterMappingTokenHandler tokenHandler = new ParameterMappingTokenHandler();
        // 获取标记解析器
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", tokenHandler);
        // 解析后的SQL
        String parseSql = genericTokenParser.parse(sql);
        // #{} 里面解析出来的参数名称
        List<ParameterMapping> parameterMappings = tokenHandler.getParameterMappings();

        return new BoundSql(parseSql, parameterMappings);
    }
}
