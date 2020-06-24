package com.shy.myinterface.impl;

import com.shy.annotation.ReturnDataType;
import com.shy.annotation.Select;
import com.shy.myenum.DataType;
import com.shy.myinterface.CrudParse;
import com.shy.utils.DataBindingUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 石皓岩
 * @create 2020-06-22 15:12
 * 描述：
 */
public class SelectParse implements CrudParse {
    @Override
    public Object parse(Object peoxy, Method method, Object[] args, Connection connection) {

        Object result = null;
        ResultSet rs = null;
        try {
            //先解析出select注解
            Select annotation = method.getAnnotation(Select.class);
            PreparedStatement ps = DataBindingUtils.parseParams(method, args, connection, annotation.value());
            rs = ps.executeQuery();
            // 封装数据
            result = getResult(method, result, rs, annotation);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Object getResult(Method method, Object result, ResultSet rs, Select annotation) {
        if (annotation.resultDataType() != null && !annotation.resultDataType().equals(Object.class)) {
            result = DataBindingUtils.autowireData(rs, annotation.resultDataType(), annotation.dataType());
        } else if (method.getAnnotation(ReturnDataType.class) != null) {
            result = DataBindingUtils.autowireData(rs, method.getAnnotation(ReturnDataType.class).value(), annotation.dataType());
        } else {
            try {
                if (method.getReturnType().equals(List.class)) {
                    Type genericReturnType = method.getGenericReturnType();
                    String typeName = genericReturnType.getTypeName();
                    String beanName = typeName.substring(typeName.indexOf("<") + 1, typeName.indexOf(">"));
                    Class<?> aClass = Class.forName(beanName);
                    result = DataBindingUtils.autowireData(rs, aClass, DataType.LIST);
                } else {
                    result = DataBindingUtils.autowireData(rs, method.getReturnType(), DataType.OBJECT);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


}
