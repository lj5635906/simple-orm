# simple-orm

简版 Mybatis orm 框架实现

主要思路

**使用端**
提供相关配置文件
SqlMapConfig.xml : 存放数据源信息，引入 mapper.xml
Mapper.xml : SQL语句配置文件

**框架端**

1、读取配置文件

Configuration : 存放数据源信息，Map<String, MappedStatement> 唯一标识(namespace.id),SQL语句

MappedStatement : SQL语句，statement类型，输入参数类型，输出参数类型

2、解析配置文件

创建SqlSessionFactoryBuilder
    使用dom4j解析配置文件，将解析出来的内容封装到 Configuration 与 MappedStatement 

创建 SqlSessionFactory ，并实现 DefaultSqlSessionFactory

3、创建 SqlSession
    实现 DefaultSqlSession，封装CURD方法：insert、delete、update、selectList、selectOne、getMapper(通过动态代理加载具体mapper)

​    创建 Executor 执行器，实现 SimpleExecutor 实现具体 JDBC 操作



**JDBC** 

```java
package com.summary;

import java.sql.*;

/**
 * @author jie.luo
 * @since 2024/8/5
 */
public class JDBCTest {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 获取数据库连接
            connection = DriverManager.getConnection("jdbc:mysql://192.168.31.100:13306/demo?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8",
                    "root", "123456");

            String sql = "select * from users where id = ?";

            // 获取预处理 statement
            preparedStatement = connection.prepareStatement(sql);

            // 设置参数
            preparedStatement.setInt(1, 1);

            // 执行查询
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");

                System.out.println(id + " : " + username);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 释放资源
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}

```

**JDBC 问题总结：**

1、数据库链接创建、释放资源造成系统资源浪费，影响系统性能。

2、Sql 语句在代码中硬编码，造成代码维护不易，实际应用中Sql变化的可能大，Sql变化需要改变java代码。

3、使用PreparedStatement向占有位符号传参数存在硬编码，因为Sql语句的where条件不一定，可能多可能少，修改Sql还需要修改代码，系统不易维护。

4、对结果集解析存在硬编码，Sql变化到账解析代码变化，系统不易维护，如果能将数据库记录封装为pojo对象解析比较方便。