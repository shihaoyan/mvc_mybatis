package com.shy.utils;


import cn.hutool.core.bean.BeanUtil;
import com.shy.annotation.*;
import com.shy.myenum.DataType;
import com.shy.servlet.MainServlet;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 石皓岩
 * @create 2020-02-28 18:38
 * 描述：该工具类是我写的一个完成自动装配的功能包
 */
@SuppressWarnings("all")
public class DataBindingUtils {
    //所有的属性保存在这里
    private static Map<String, Field> allFields = new ConcurrentHashMap<>();
    //因为最后需要进行一些字符串比对，会经过一些转换
    //这个里面维护了原始字符串  关系：转换后的->原始字符串
    private static Map<String, String> alias = new ConcurrentHashMap<>();
    // 这个是维护bean类型的映射表
    private static Map<String, Map<String, Field>> beanMap = new ConcurrentHashMap<>();


    private static void cuttingParameters(Map<String, Object> map, String s1) {

        String[] split1 = s1.split("=");
        if (split1.length == 2) {
            String name = split1[0];
            String value = split1[1];
            //进行字符串处理
            name = getNotUnderlineString(name);
            map.put(name, value);
        } else if (split1.length == 1) {
            String name = split1[0];
            map.put(name, "");
        }
    }


    /**
     * 自动封装参数，我们可以自己调用，但是也可以不用，因为我们自己进行调用了，当我们调用的方法有参数的时候，
     * 会调用这里进行自动封装数据
     *
     * @param request
     * @param dataType
     * @param <T>
     * @return
     */
    public static <T> T autowriteParameter(HttpServletRequest request, Class dataType) {
        if (request == null) {
            throw new RuntimeException("request不能为空");
        }
        if (dataType == null) {
            throw new RuntimeException("class类型不能为空");
        }
        try {
            // 得到对象并且会把Field进行映射
            Object o = getObjectAndIntegrateMap(dataType, DataType.REQUEST);
            // 设置常见类型
            setCommonParam(o, request);
            // 设置bean类型的参数
            setBeanParam(o, request);
            return (T) o;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * 设置通用参数，private 不允许其他人调用，用来从reuqest中获取参数并设置
     *
     * @param o
     * @param request
     */
    private static void setCommonParam(Object o, HttpServletRequest request) {
        Set<String> keySet1 = allFields.keySet();
        Iterator<String> iterator = keySet1.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Field field = allFields.get(key);
            String value = request.getParameter(key);
            if (isCommonType(field.getType())) {
                setCommonType(o, value, field);
                iterator.remove();
            }
        }
    }

    /**
     * 设置bean参数，private 不允许其他人调用，用来从reuqest中获取参数并设置
     *
     * @param o
     * @param request
     */
    private static void setBeanParam(Object o, HttpServletRequest request) throws InstantiationException, IllegalAccessException {
        Set<String> keySet = allFields.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            Field field = allFields.get(name);
            Object o1 = field.getType().newInstance();
            if (beanMap.containsKey(name)) {
                Map<String, Field> stringFieldMap = beanMap.get(name);
                Set<String> keySet1 = stringFieldMap.keySet();
                Iterator<String> iterator1 = keySet1.iterator();
                while (iterator1.hasNext()) {
                    String next = iterator1.next();
                    String parameter = request.getParameter(next);
                    if (parameter == null) {
                        continue;
                    }
                    if (stringFieldMap.containsKey(next)) {
                        Field child = stringFieldMap.get(next);
                        setCommonType(o1, parameter, child);
                        iterator1.remove();
                    }
                }
                field.set(o, o1);
                iterator.remove();
                beanMap.remove(name);
            }
        }
    }

    private static Object getObjectAndIntegrateMap(Class dataType, DataType tableOrJsp) throws InstantiationException, IllegalAccessException {
        allFields.clear();
        alias.clear();
        //拿到这个对象的所有属性
        Object o = dataType.newInstance();
        Field[] all = getAllFields(dataType);
        //把所有的属性整合到一起
        //里面涉及到了转换字符串的算法，就是把所有的字符串转换成小写
        //如果有_就删除掉
        doTransFormMap(all, tableOrJsp);
        return o;
    }

    public static Field[] getAllFields(Class dataType) {
        Field[] declaredFields = dataType.getDeclaredFields();
        Field[] declaredFields1 = dataType.getSuperclass().getDeclaredFields();
        Field[] all = new Field[declaredFields.length + declaredFields1.length];
        System.arraycopy(declaredFields, 0, all, 0, declaredFields.length);
        System.arraycopy(declaredFields1, 0, all, declaredFields.length, declaredFields1.length);
        return all;
    }


    /**
     * 我自己写的封装数据的方法他会根据你传递进来的rs进行遍历寻找出查询出来的数据，
     * class是将要封装成的对象
     * dataType是将要返回值的类型  支持List和Object
     * 最后一个参数是后置处理器
     *
     * @param rs
     * @param
     * @param
     * @return
     */

    public static <T> T autowireData(ResultSet rs, Class clazz, DataType dataType) {
        if (rs == null) {
            throw new RuntimeException("ResultSet不能为空");
        }
        if (clazz == null) {
            throw new RuntimeException("class类型不能为空");
        }
        //用来保存数据的
        List data = new ArrayList<>();
        //遍历数据
        try {
            while (rs.next()) {
                //创建实例
                Object o = DataBindingUtils.getObjectAndIntegrateMap(clazz, DataType.TABLE);
                //拿到所有的rs中的列明
                //进行自动装配
                doAssembly(rs, o);
                //装入list
                data.add(o);
            }
        } catch (Exception e) {
            throw new RuntimeException("数据封装失败，请检查将要封装的类型和查询出来的类型是否一致");
        }
        if (dataType == null || dataType.equals(DataType.OBJECT)) {
            if (data.size() == 0) {
                return null;
            }
            return (T) data.get(0);
        } else if (dataType.equals(DataType.LIST)) {
            if (data.size() == 0) {
                return null;
            }
            return (T) data;
        }
        return null;
    }

    private static void doAssembly(ResultSet rs, Object o) throws SQLException, IllegalAccessException {
        ResultSetMetaData metaData = rs.getMetaData();
        int i = 1;
        int temp;
        for (; i <= metaData.getColumnCount(); i++) {
            //这一步拿到原值，并且原值和转换后的值做了映射放到了alias这个别名集合中
            String columnName = getSimpleColumnName(metaData.getColumnName(i));
            // 直接封装普通对象
            if (allFields.containsKey(columnName)) {
                Field field = allFields.get(columnName);
                setCommonType(o, String.valueOf(rs.getObject(alias.get(columnName))), field);
                //我给你一次对每一列重新匹配的机会，并且给你重新改变对象的机会
                invokePostProcesser(rs, o, columnName, field);
                allFields.remove(columnName);
                alias.remove(columnName);
            } else {
                try {
                    temp = i;
                    Iterator<String> iterator1 = beanMap.keySet().iterator();
                    while (iterator1.hasNext()) {
                        i = temp;
                        Object child = null;
                        String beanName = iterator1.next();
                        if (allFields.containsKey(beanName)) {
                            // 这个是teacher的filed
                            Field field = allFields.get(beanName);
                            if (child == null) {
                                child = field.getType().newInstance();
                            }
                            Map<String, Field> stringFieldMap = beanMap.get(beanName);
                            for (; i <= metaData.getColumnCount(); i++) {
                                columnName = getSimpleColumnName(metaData.getColumnName(i));
                                if (stringFieldMap.containsKey(columnName)) {
                                    Field childField = stringFieldMap.get(columnName);
                                    if (rs.getObject(columnName) == null) {
                                        continue;
                                    }
                                    setCommonType(child, String.valueOf(rs.getObject(alias.get(columnName))), childField);
                                    stringFieldMap.remove(columnName);
                                    alias.remove(columnName);
                                } else {
                                    beanMap.remove(beanName);
                                    allFields.remove(beanName);
                                    alias.remove(columnName);
                                }
                            }
                            field.set(o, child);
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static String getSimpleColumnName(String name) {
        String columnName = name.toLowerCase();
        //进行字符串处理,就是得到去掉下划线的列名
        columnName = getNotUnderlineString(columnName);
        alias.put(columnName, name);
        return columnName;
    }

    private static void setCommonType(Object o, String value, Field field) {
        try {
            if (value == null) {
                return;
            } else if (field.getType().equals(String.class)) {
                field.set(o, value);
            } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                field.set(o, Integer.parseInt(value));
            } else if (field.getType().equals(double.class) || field.getType().equals(Double.class)) {
                field.set(o, Double.parseDouble(value));
            } else if (field.getType().equals(float.class) || field.getType().equals(Float.class)) {
                field.set(o, Float.parseFloat(value));
            } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                field.set(o, Long.parseLong(value));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void invokePostProcesser(ResultSet rs, Object o, String columnName, Field field) {
        Set<Class> classes = MainServlet.singleMap.keySet();
        for (Class aClass : classes) {
            if (aClass.getInterfaces().length > 0 && aClass.getInterfaces()[0].equals(DataPostProcessor.class)) {
                DataPostProcessor dataPostProcessor = (DataPostProcessor) MainServlet.singleMap.get(aClass);
                dataPostProcessor.typeOfContrast(rs, field, o, columnName);
                dataPostProcessor.changeObject(o);
            }
        }
    }

    private static void doTransFormMap(Field[] all, DataType dataType) {
        for (Field field : all) {
            field.setAccessible(true);
            // 我想当解析每个类型的时候，手首先进行一个类型判断
            if (!isCommonType(field.getType())) {
                String name = getCommonFieldName(field, dataType);
                Map<String, Field> map = new ConcurrentHashMap<>();
                Field[] all1 = getAllFields(field.getType());
                doTransFormMap(all1, dataType);
                for (Field field1 : all1) {
                    field1.setAccessible(true);
                    String childName = getCommonFieldName(field1, dataType);
                    map.put(childName, field1);
                    allFields.put(name, field);
                }
                beanMap.put(name, map);
            } else {
                String name = getCommonFieldName(field, dataType);
                //这个是为了防止父类的属性覆盖
                if (!allFields.containsKey(name)) {
                    allFields.put(name, field);
                }
            }

        }
    }

    private static String getCommonFieldName(Field field, DataType dataType) {
        String name = field.getName().toLowerCase();
        //接下来需要处理注解,如果定义了注解就需要进行解析注解
        if (dataType == null || dataType.equals(DataType.TABLE)) {
            ColumnName annotation = field.getAnnotation(ColumnName.class);
            if (annotation != null && !annotation.value().equals("")) {
                name = annotation.value().toLowerCase();
            }
        } else if (dataType.equals(DataType.REQUEST)) {
            JspParameter annotation = field.getAnnotation(JspParameter.class);
            if (annotation != null && !annotation.value().equals("")) {
                name = annotation.value().toLowerCase();
            }
        }
        //转换为没有下划线的字符串
        name = getNotUnderlineString(name);
        return name;
    }

    /**
     * 抽取出来的方法，得到没有下划线的字符串
     *
     * @param name
     * @return
     */
    public static String getNotUnderlineString(String name) {
        //首先验证参数，name不能为空否则抛出 异常
        if (name == null) {
            throw new RuntimeException("当转换字符串的时候，name不能为空");
        }
        if (name.contains("_")) {
            String[] s = name.split("_");
            name = "";
            for (String s1 : s) {
                name += s1;
            }
        }
        return name;
    }

    public static boolean isBean(Class<?> parameterType) {


        /*//如果是基本类型直接返回
        if (isCommonType(parameterType)) {
            return false;
        }
        // 怎么判断一个Class是bean类型呢？
        // 我觉得应该是我们对bean的定义吧，首先我觉得应该是对注解的判断，如果添加了bean注解没什么好说的，直接就是了
        if (parameterType.getAnnotation(Bean.class) != null) {
            return true;
        }
        boolean flag = false;
        // 2.我觉得应该拿到所有的属性，判断是否所有的属性都私有
        Field[] allFields = new Field[parameterType.getDeclaredFields().length + parameterType.getSuperclass().getDeclaredFields().length];
        Field[] fields = parameterType.getDeclaredFields();
        Field[] superFields = parameterType.getSuperclass().getDeclaredFields();
        System.arraycopy(fields, 0, allFields, 0, fields.length);
        System.arraycopy(superFields, 0, allFields, fields.length, superFields.length);

        return isHasGetterAndSetter(parameterType, allFields);*/
        // 上面的代码 对bean的要求实在是太严格了，所以抛弃了
        return BeanUtil.isBean(parameterType);
    }

    public static boolean isCommonType(Class<?> parameterType) {
        if (parameterType.equals(String.class)) {
            return true;
        } else if (parameterType.equals(int.class) || parameterType.equals(Integer.class)) {
            return true;
        } else if (parameterType.equals(double.class) || parameterType.equals(Double.class)) {
            return true;
        } else if (parameterType.equals(float.class) || parameterType.equals(Float.class)) {
            return true;
        } else if (parameterType.equals(long.class) || parameterType.equals(Long.class)) {
            return true;
        } else if (parameterType.equals(short.class) || parameterType.equals(Short.class)) {
            return true;
        } else if (parameterType.equals(byte.class) || parameterType.equals(Byte.class)) {
            return true;
        } else if (parameterType.equals(char.class) || parameterType.equals(Character.class)) {
            return true;
        } else if (parameterType.equals(boolean.class) || parameterType.equals(Boolean.class)) {
            return true;
        }
        return false;
    }

    private static boolean isHasGetterAndSetter(Class<?> parameterType, Field[] fields) {
        boolean flag = false;
        for (Field field : fields) {
            if (field.isAccessible()) {
                break;
            }
            // 判断是否存在getter/setter方法
            String name = field.getName();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            String getter = "get" + name;
            String setter = "set" + name;
            try {
                if (parameterType.getMethod(getter) == null) {
                    flag = false;
                    break;
                }
                if (parameterType.getMethod(setter, field.getType()) == null) {
                    flag = false;
                    break;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            flag = true;
        }
        return flag;
    }

    public static PreparedStatement parseParams(Method method, Object[] args, Connection connection, String sql) throws SQLException {
        // 解析出sql语句中所有的占位符，按照顺序放到一个数组中
        Map<Integer, String> map = new HashMap<>();
        sql = StringUtils.transformPlaceholders(sql, map);
        PreparedStatement ps = connection.prepareStatement(sql);
        // 这里包含了所有的通过注解标注的参数名称
        String[] params = ParameterNameUtils.getMethodParameterNamesByAnnotation(method);
        //判断是否有参数
        if (method.getParameterCount() != 0) {
            // 现在的sql就是常规的直接是占位符 ？ 的sql语句了
            // 现在开始解析参数
            // String username,String password
            // select * from password = #{password} and username = #{username}
            // 这一步解决所有的参数都是通用类型的问题
            int i = 0;
            if (params != null && params.length > 0) {
                for (; i < params.length; i++) {
                    if (DataBindingUtils.isCommonType(args[i].getClass())) {
                        Set<Integer> integers = map.keySet();
                        Iterator<Integer> iterator = integers.iterator();
                        while (iterator.hasNext()) {
                            Integer index = iterator.next();
                            String s = map.get(index);
                            if (params[i].equals(s.substring(2, s.length() - 1))) {
                                ps.setObject(index, args[i]);
                                iterator.remove();
                                break;
                            }
                        }
                    }
                }
            }
            // 接下来解决当参数中存在bean类型的变量
            if (args.length > params.length) {
                for (; i < args.length; i++) {
                    if (DataBindingUtils.isBean(args[i].getClass())) {
                        // 然后对比map看看那个field的值和map中的值相同
                        Set<Integer> integers = map.keySet();
                        Iterator<Integer> iterator = integers.iterator();
                        while (iterator.hasNext()) {
                            Integer index = iterator.next();
                            String s = map.get(index);
                            // 直接拿到指定Field就行了吧？？？
                            try {
                                Field field = args[i].getClass().getDeclaredField(s.substring(2, s.length() - 1));
                                field.setAccessible(true);
                                // 然后通过对象直接拿到这个Field的值
                                Object o = field.get(args[i]);
                                // 设置进rs
                                ps.setObject(index, o);
                                iterator.remove();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return ps;
    }
}
