package com.shy.proxy;

import com.shy.annotation.*;
import com.shy.myinterface.CrudParse;
import com.shy.myinterface.impl.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * @author 石皓岩
 * @create 2020-06-08 12:04
 * 描述：
 */
public class RemoteInvocationHandler implements InvocationHandler {

    private Class interfaceClass;
    private Connection connection;

    public RemoteInvocationHandler() {

    }

    public RemoteInvocationHandler(Class interfaceClass, Connection connection) {
        this.interfaceClass = interfaceClass;
        this.connection = connection;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {

        Object result = null;
        // 就两种情况，第一种查询
        if (method.getAnnotation(Select.class) != null) {
            result = parse(new SelectParse(), proxy, method, args, connection);
        } else if (method.getAnnotation(Insert.class) != null) {
            // 插入
            parse(new InsertParse(), proxy, method, args, connection);
        } else if (method.getAnnotation(Update.class) != null) {
            // 更新
            parse(new UpdateParse(), proxy, method, args, connection);
        } else if (method.getAnnotation(Delete.class) != null) {
            // 更新
            parse(new DeleteParse(), proxy, method, args, connection);
        } else if (method.getAnnotation(NotQuery.class) != null) {
            // 更新
            parse(new NotQueryParse(), proxy, method, args, connection);
        }
        return result;
    }

    public Object parse(CrudParse crudParse, Object proxy, Method method, Object[] args, Connection connection) {
        return crudParse.parse(proxy, method, args, connection);
    }
}
