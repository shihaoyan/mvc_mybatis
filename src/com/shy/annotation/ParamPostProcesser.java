package com.shy.annotation;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * @author 石皓岩
 * @create 2020-06-16 11:34
 * 描述：
 */
public interface ParamPostProcesser {

    /**
     * 扩展方法，当类型匹配不够的时候，可以进行这个接口的扩展比对
     * 就是当我们参数中存在一个或多个bean实体类的时候，可以在这里进行判断进行添加
     *
     * @param request 请求参数
     * @param field   对象的属性
     * @param o       当前对象
     */
    default boolean typeOfContrast(HttpServletRequest request, Field field, Object o) {
        return false;
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
