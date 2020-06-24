package com.shy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 石皓岩
 * @create 2020-02-29 11:17
 * 描述：这个是弥补因为创建bean的时候没有和数据库中的列名进行对其而无法进行自动注入的问题而开发的注解
 * 提供一个默认值，当标注了这个注解但是没有赋值的话，会使用默认的列名
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnName {
    /**
     * 对应数据库中的列名
     * @return
     */
    String value() default "";
}
