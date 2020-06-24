package com.shy.myenum;

/**
 * @author 石皓岩
 * @create 2020-02-28 15:51
 * 描述：数据类型
 */
public enum DataType {
    /**
     * 封装数据的时候。返回值是List集合
     */
    LIST,
    /**
     * 封装数据的时候，返回值是单个对象
     */
    OBJECT,
    /**
     * 表类型
     */
    TABLE,
    /**
     * request中参数的类型，用来封装数据的
     */
    REQUEST
}
