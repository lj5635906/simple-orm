package com.summary.mapper;

import com.summary.pojo.User;

import java.util.List;

/**
 * @author jie.luo
 * @since 2024/8/5
 */
public interface UserMapper {
    List<User> selectList();

    User selectOne(User user);

    int saveUser(User user);

    int updateUser(User user);

    int deleteUser(Integer id);
}
