package com.shy.annotation;

import java.lang.annotation.*;

/**
 * @author 石皓岩
 * @create 2020-06-15 22:43
 * 描述：注解的作用是，根据类型进行自动注入，但是注入的对象必须是标注了WebModule,Service,Dao,Module注解才能
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoInstantiate {



}
