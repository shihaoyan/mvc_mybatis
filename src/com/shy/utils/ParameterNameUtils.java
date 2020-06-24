package com.shy.utils;

import com.shy.annotation.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author 石皓岩
 * @create 2020-06-15 18:57
 * 描述：
 */
public class ParameterNameUtils {
    /**
     * 获取指定方法的参数名
     *
     * @param method 要获取参数名的方法
     * @return 按参数顺序排列的参数名列表
     */
    public static String[] getMethodParameterNamesByAnnotation(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        int num = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                Class<? extends Annotation> aClass = annotation.annotationType();
                if (aClass == Param.class) {
                    num++;
                }
            }
        }
        String[] parameterNames = new String[num];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Param) {
                    Param param = (Param) annotation;
                    parameterNames[i++] = param.value();
                }
            }
        }
        return parameterNames;
    }
}
