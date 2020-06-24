package com.shy.service;

import com.shy.beans.User;

import java.util.List;

/**
 * @author 石皓岩
 * @create 2020-06-18 13:12
 * 描述：
 */
public interface UserService {
    List<User> findAllUser();

    void updateState(Integer uid, Integer state);

    User findUserById(Integer uid);

    void update(User user);
}
