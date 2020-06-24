package com.shy.annotation;

import java.lang.annotation.*;

/**
 * @author 石皓岩
 * @create 2020-06-15 20:39
 * 描述：就是把数据直接写到前台界面，不用什么转发啥的
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {


}
