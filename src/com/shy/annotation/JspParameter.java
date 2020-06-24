package com.shy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 石皓岩
 * @create 2020-02-29 11:58
 * 描述：这个注解的作用是，当进行前台界面和bean封装的时候，如果前台界面的数值和bean不相同的话，可以进行微调
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JspParameter {
    /**
     * 对应jsp界面的参数，默认使用属性名
     * @return
     */
    String value() default "";
}
