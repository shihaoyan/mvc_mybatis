package com.shy.annotation;

import com.shy.beans.Admin;
import com.shy.myenum.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 石皓岩
 * @create 2020-06-22 14:33
 * 描述：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {
    /**
     * sql语句
     *
     * @return
     */
    String value();

    /**
     * 返回值类型,用于封装数据,如果不写他的话 需要标注另一个注解，@ReturnDataType
     *
     * @return
     */
    Class resultDataType() default Object.class;


    /**
     * 返回的是List还是Object啊，默认的话 是返回List
     *
     * @return
     */
    DataType dataType() default DataType.LIST;


}
