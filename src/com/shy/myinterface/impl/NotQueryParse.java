package com.shy.myinterface.impl;

import com.shy.annotation.NotQuery;
import com.shy.annotation.Update;
import com.shy.myinterface.CrudParse;
import com.shy.utils.DataBindingUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 石皓岩
 * @create 2020-06-24 12:38
 * 描述：
 */
public class NotQueryParse implements CrudParse {
    @Override
    public Object parse(Object peoxy, Method method, Object[] args, Connection connection) {
        Object result = null;
        ResultSet rs = null;
        try {
            //先解析出select注解
            NotQuery annotation = method.getAnnotation(NotQuery.class);
            PreparedStatement ps = DataBindingUtils.parseParams(method, args, connection, annotation.value());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
