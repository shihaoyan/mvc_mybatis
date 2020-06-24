package com.shy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 石皓岩
 * @create 2020-06-16 0:52
 * 描述：这个注解就是我们自己的组件注解，就是说你把一个类标注了这个注解，就会进行自动注入
 * 类似于spring的compent注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {



}
