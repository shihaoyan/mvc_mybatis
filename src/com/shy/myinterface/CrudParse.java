package com.shy.myinterface;

import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * @author 石皓岩
 * @create 2020-06-22 15:06
 * 描述：
 */
public interface CrudParse {

    Object parse(Object peoxy, Method method, Object[] args, Connection connection);

}
