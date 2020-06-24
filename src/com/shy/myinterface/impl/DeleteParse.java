package com.shy.myinterface.impl;

import com.shy.annotation.Delete;
import com.shy.annotation.Insert;
import com.shy.myinterface.CrudParse;
import com.shy.utils.DataBindingUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 石皓岩
 * @create 2020-06-24 12:31
 * 描述：
 */
public class DeleteParse implements CrudParse {
    @Override
    public Object parse(Object peoxy, Method method, Object[] args, Connection connection) {
        Object result = null;
        ResultSet rs = null;
        try {
            //先解析出select注解
            Delete annotation = method.getAnnotation(Delete.class);
            PreparedStatement ps = DataBindingUtils.parseParams(method, args, connection, annotation.value());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
