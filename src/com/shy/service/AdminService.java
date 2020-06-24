package com.shy.service;

import com.shy.beans.Admin;

/**
 * @author 石皓岩
 * @create 2020-06-18 12:17
 * 描述：
 */
public interface AdminService {

    Admin login(String account, String password);
}
