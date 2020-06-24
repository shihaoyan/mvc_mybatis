package com.shy.processor;

import com.shy.annotation.DataPostProcessor;

/**
 * @author 石皓岩
 * @create 2020-06-16 9:06
 * 描述：这是我提供的后置处理器，我们可以通过这里清晰的看到每个对象的每一个字段进行赋值的详情信息
 * 如果我们想要改变这个类的话，直接用changeObject方法就行了。
 * 如果我们数据库中的字段类型不是几个常见的类型的话如int(Integer) String Date double这几种的话
 * 我们需要重写转换方法，就是另一个方法，这个方法你可以进行比较和设置值。
 * 当然这个后置处理器怎么使用，很简单实现接口，并且添加组件注解Module
 */
//@Module  //不想用的话 关掉这个注解，表示不用框架自动实例化管理。
public class MyDataPostProcessor implements DataPostProcessor {

}
