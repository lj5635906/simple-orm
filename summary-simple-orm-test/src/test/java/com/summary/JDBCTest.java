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
