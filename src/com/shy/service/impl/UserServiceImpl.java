package com.shy.service.impl;

import com.shy.annotation.AutoInstantiate;
import com.shy.annotation.Service;
import com.shy.beans.User;
import com.shy.dao.UserDao;
import com.shy.service.UserService;

import java.util.List;

/**
 * @author 石皓岩
 * @create 2020-06-18 13:14
 * 描述：
 */
@Service
public class UserServiceImpl implements UserService {

    @AutoInstantiate
    private UserDao userDao;


    @Override
    public List<User> findAllUser() {
        return userDao.findAllUser();
    }

    @Override
    public void updateState(Integer uid, Integer state) {
        userDao.updateState(uid,state);
    }

    @Override
    public User findUserById(Integer uid) {
        return userDao.findUserById(uid);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }
}
