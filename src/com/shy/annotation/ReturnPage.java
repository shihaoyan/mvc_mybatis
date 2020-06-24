package com.shy.annotation;

import java.lang.annotation.*;

/**
 * @author 石皓岩
 * @create 2020-06-15 20:06
 * 描述：这个注解就是视图解析器，就是当你不设置的时候，默认路径是下面两个前缀后缀加上自己的返回值
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReturnPage {


    /**
     * 前缀是/WEB-INF
     * @return
     */
    String prefix() default "/WEB-INF/pages/";

    /**
     * 默认后缀是.jsp
     * @return
     */
    String suffix() default ".jsp";



}
