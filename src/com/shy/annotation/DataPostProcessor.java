package com.shy.annotation;

import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * @author 石皓岩
 * @create 2020-02-28 16:13
 * 描述：这是后置处理器接口，当我们后面需要扩展类型的比对的时候会调用这个接口
 * 比如说我们的属性有了一个新的类型，并且这个类型还对应着数据库中的类型
 * 我们就需要继承这个接口,并重写方法。
 */
public interface DataPostProcessor {
    /**
     * 扩展方法，当类型匹配不够的时候，可以进行这个接口的扩展比对
     * 模板：
     *  if (field.getType().equals(int.class)) {
     *     field.set(o, rs.getInt(columnName));
     *  }
     * @param rs    数据集
     * @param field 对象的属性
     * @param o        当前对象
     * @param columnName  数据库中的列名
     */
    default void typeOfContrast(ResultSet rs, Field field, Object o, String columnName) {

    }

    /**
     * 提供一个更改查询出来的对象的机会，当执行完类型比对之后，会进行一次对象改变
     * 可以不重写这个方法，提供默认值了
     *
     * @param o
     * @return
     */
    default Object changeObject(Object o) {
        return o;
    }
}
