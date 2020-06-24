package com.shy.service.impl;

import com.shy.annotation.AutoInstantiate;
import com.shy.annotation.Service;
import com.shy.beans.Admin;
import com.shy.beans.Category;
import com.shy.dao.AdminDao;
import com.shy.service.AdminService;

/**
 * @author 石皓岩
 * @create 2020-06-18 12:18
 * 描述：
 */
@Service
public class AdminServiceImpl implements AdminService {

    @AutoInstantiate
    private AdminDao adminDao;


    @Override
    public Admin login(String account, String password) {
        return adminDao.login(account,password);
    }
}
