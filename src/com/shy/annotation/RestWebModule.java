package com.shy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 石皓岩
 * @create 2020-06-20 21:45
 * 描述：类似于RestController，就是标注这个注解，所有的handler都会直接返回到界面，相当于所有的方法都标注了
 * @ResponseBody注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestWebModule {
}
