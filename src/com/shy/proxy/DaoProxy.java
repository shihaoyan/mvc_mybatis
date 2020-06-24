package com.shy.proxy;

import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author 石皓岩
 * @create 2020-06-08 12:02
 * 描述：
 */
public class DaoProxy {
    public static Object getProxy(Class interfaceClass, Connection connection){
        return Proxy.newProxyInstance(DaoProxy.class.getClassLoader(),new Class[]{interfaceClass},new RemoteInvocationHandler(interfaceClass,connection));
    }
}
