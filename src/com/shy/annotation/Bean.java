package com.shy.annotation;

import java.lang.annotation.*;

/**
 * @author 石皓岩
 * @create 2020-06-16 1:06
 * 描述：所有的bean实体类当要在方法参数中进行自动封装数据的时候，必须标注这个注解
 * 说实话这个设置的很傻逼，但是我没办法。。。如果直接直接判断是否是一个bean的话实在是很麻烦，
 * 所以我就选一个方便点的方式。
 *
 * 后面做了一个维护，这个可加可不加，加的话会提高效率和准确率。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

}
