package com.shy.annotation;

import java.lang.annotation.*;

/**
 * @author 石皓岩
 * @create 2020-06-15 18:56
 * 描述：参数注解，当我们想要在参数中获取参数值的时候，每一个变量必须标识一下这个，它的值就是前台传递过来的对象的值
 * 请注意，名字必须和传递的参数名相同
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Param {
    String value();
}
