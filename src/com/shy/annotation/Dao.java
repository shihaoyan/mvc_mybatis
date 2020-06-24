package com.shy.annotation;

import java.lang.annotation.*;

/**
 * @author 石皓岩
 * @create 2020-06-15 22:36
 * 描述：Dao层注解，标注了这个注解就会在程运行的第一次就会进行实例化，一会就能为其他的层进行实例化注入
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dao {
}
