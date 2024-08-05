package com.summary;

import com.summary.mapper.UserMapper;
import com.summary.pojo.User;
import com.summary.simple.orm.io.Resources;
import com.summary.simple.orm.session.SqlSession;
import com.summary.simple.orm.session.SqlSessionFactory;
import com.summary.simple.orm.session.SqlSessionFactoryBuilder;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void beforeTest() throws Exception {
        // 加载配置文件
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        // 构建 SqlSessionFactory
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
    }

    @Test
    public void testInsert() {
        // 获取 SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        for (int i = 1; i <= 6; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("username_" + i);
            int row = userMapper.saveUser(user);
            Assert.assertEquals(row, 1);
        }
    }

    @Test
    public void testDelete() {
        // 获取 SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int row = userMapper.deleteUser(6);
        Assert.assertEquals(row, 1);
    }

    @Test
    public void testUpdate() {
        // 获取 SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int row = userMapper.updateUser(new User() {{
            this.setId(5);
            this.setUsername("username_update");
        }});
        Assert.assertEquals(row, 1);
    }

    @Test
    public void testSelectList() {
        // 获取 SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.selectList();
        System.out.println(users);
    }

    @Test
    public void testSelectOne() {
        // 获取 SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.selectOne(new User() {{
            this.setId(1);
            this.setUsername("username_1");
        }});
        System.out.println(user);
    }
}
