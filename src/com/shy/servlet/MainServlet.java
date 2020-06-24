package com.shy.servlet;

import cn.hutool.json.JSONUtil;
import com.shy.annotation.*;
import com.shy.proxy.DaoProxy;
import com.shy.utils.ClassUtils;
import com.shy.utils.DataBindingUtils;
import com.shy.utils.JDBCUtils;
import com.shy.utils.ParameterNameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 石皓岩
 * @create 2020-06-15 17:43
 * 描述：
 */
@WebServlet("/")
public class MainServlet extends HttpServlet {

    /**
     * 用来保存映射关系的
     * /student/list -> method
     */
    private static Map<String, Method> methodMap = new ConcurrentHashMap<>();

    /**
     * 保存所有的标注了WebModule注解的类
     */
    private static List<Class> webModuleClasses = new CopyOnWriteArrayList<>();
    /**
     * 保存了方法和实例化对象的缓存
     * method -> 这个方法的类的对象
     */
    private static Map<Method, Object> methodObjectMap = new ConcurrentHashMap<>();
    /**
     * 实例化池
     * Class -> 实例化对象
     */
    public static Map<Class, Object> singleMap = new ConcurrentHashMap<>();

    /**
     * 默认返回界面的前缀和后缀
     */
    private final static String PREFIX = "/jsp/";
    private final static String SUFFIX = ".jsp";

    /**
     * 请求转发
     */
    private final static String FORWARD = "forward";
    /**
     * 请求重定向
     */
    private final static String REDIRECT = "redirect";
    /**
     * 默认的错误界面
     */
    private final static String ERROR_PAGE = "/404.jsp";

    /**
     * 这个用来保存request域中的参数
     */
    private final static Map<String, Object> REQUESTPARAM = new ConcurrentHashMap<>();

    /**
     * 数据库连接对象
     */
    private final static Connection connection = JDBCUtils.getConnection();


    static {
        //1.扫描所有的包，并拿到该项目所有的类
        List<Class> classes = scanAllModule();
        //2.搞一个实例化缓存池,直接在下面的循环中实例化了。拿到所有的标注了WebModule的注解类
        instantiationModule(classes);
        //3.拿到webModule类的所有方法
        HandllerMapping();
        //4.自动注入功能
        autoInstantiation();
    }

    private static void HandllerMapping() {
        for (Class webModuleClass : webModuleClasses) {
            Method[] methods = webModuleClass.getMethods();
            //4.拿到方法上的所有注解RequestMapping的方法，并且放到一个map映射中
            for (Method method : methods) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                if (annotation != null) {
                    //如果一个方法标注了RequestMapping注解，那就拿到它的值
                    String value = annotation.value();
                    //把当前方法和字符串进行映射
                    methodMap.put(value, method);
                    try {
                        methodObjectMap.put(method, singleMap.get(webModuleClass));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void instantiationModule(List<Class> classes) {
        for (Class aClass : classes) {
            // 实例化所有的module
            if (aClass.isInterface() && aClass.getAnnotation(Dao.class) == null) {
                continue;
            }
            try {
                if (aClass.getAnnotation(WebModule.class) != null) {
                    webModuleClasses.add(aClass);
                    singleMap.put(aClass, aClass.newInstance());
                } else if (aClass.getAnnotation(Service.class) != null) {
                    singleMap.put(aClass, aClass.newInstance());
                } else if (aClass.getAnnotation(Dao.class) != null) {
                    if(aClass.isInterface()){
                        singleMap.put(aClass, DaoProxy.getProxy(aClass, connection));
                    }else {
                        singleMap.put(aClass, aClass.newInstance());
                    }
                } else if (aClass.getAnnotation(Module.class) != null) {
                    singleMap.put(aClass, aClass.newInstance());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    /**
     * 自动实例化
     */
    private static void autoInstantiation() {
        Set<Class> classes1 = singleMap.keySet();
        for (Class aClass : classes1) {
            // 拿到所有的属性
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                // 判断是否添加了自动注入
                AutoInstantiate annotation = declaredField.getAnnotation(AutoInstantiate.class);
                if (annotation != null) {
                    // 拿到这个属性的类型
                    Class<?> type = declaredField.getType();
                    try {
                        // 如果是接口的话，就要进行类型注入
                        if (type.isInterface()) {
                            for (Class aClass1 : classes1) {
                                Class[] interfaces = aClass1.getInterfaces();
                                for (Class anInterface : interfaces) {
                                    if (anInterface.equals(type)) {
                                        type = aClass1;
                                    }
                                }
                            }
                        }
                        declaredField.set(singleMap.get(aClass), singleMap.get(type));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码
        setCharacterEncoding(request, response, "UTF-8");
        //5.拿到请求的路径，并找出相应的方法。  /student
        Method method = getRequestMappingMethod(request, response);
        //6.通过注解解析出方法的所有参数
        Object[] args = getArgsAndBindParam(request, response, method);
        //9.执行方法
        Object object = invokeMethodAndBindMap(request, method, args);
        //10.执行视图解析
        viewResolver(request, response, method, object);
    }

    private void viewResolver(HttpServletRequest request, HttpServletResponse response, Method method, Object object) throws IOException, ServletException {
        // 这一步是为了判断是否需要直接返回JSON串
        if (!returnJson(response, method, object)) {
            // 如果没有的话 直接进行跳转界面
            gotoPage(request, response, method, object);
        }
    }

    private boolean returnJson(HttpServletResponse response, Method method, Object object) throws IOException {
        ResponseBody annotation1 = method.getAnnotation(ResponseBody.class);
        Object o = methodObjectMap.get(method);
        RestWebModule annotation = o.getClass().getAnnotation(RestWebModule.class);
        if (annotation1 != null || annotation != null) {
            // 这里需要通过json工具进行转化 但是我没弄
            String s = JSONUtil.toJsonStr(object);
            response.setCharacterEncoding("GBK");
            response.getWriter().write(s);
            return true;
        }
        return false;
    }

    private void setCharacterEncoding(HttpServletRequest request, HttpServletResponse response, String character) throws UnsupportedEncodingException {
        request.setCharacterEncoding(character);
        response.setCharacterEncoding(character);
    }

    /**
     * 如果没加注解的话首先判断是否是请求转发和请求重定向
     *
     * @param request
     * @param response
     * @param method   执行的方法
     * @param object   执行方法的返回值
     * @throws ServletException
     * @throws IOException
     */
    private void gotoPage(HttpServletRequest request, HttpServletResponse response, Method method, Object object) throws ServletException, IOException {
        String invoke = String.valueOf(object);
        // 判断一下是否直接跳转界面
        if (invoke.contains(":")) {
            String[] split = invoke.split(":");
            if (split[0].equals(FORWARD)) {
                // 直接进行请求转发
                request.getRequestDispatcher(split[1]).forward(request, response);
            }
            if (split[0].equals(REDIRECT)) {
                response.sendRedirect(split[1]);
            }
        } else {
            // 都没有的话直接进行默认跳转界面
            String path = PREFIX + invoke + SUFFIX;
            // 通过返回值跳转界面,拼接字符串,进行跳转界面
            //首先确定一下当前方法是否配置了前缀和后缀
            Object o = methodObjectMap.get(method);
            ReturnPage annotation1 = o.getClass().getAnnotation(ReturnPage.class);
            if (annotation1 != null) {
                path = annotation1.prefix() + invoke + annotation1.suffix();
            }
            ReturnPage annotation = method.getAnnotation(ReturnPage.class);
            if (annotation != null) {
                path = annotation.prefix() + invoke + annotation.suffix();
            }
            // 返回界面
            request.getRequestDispatcher(path).forward(request, response);
        }

    }

    private Object invokeMethodAndBindMap(HttpServletRequest request, Method method, Object[] args) {
        Object o = methodObjectMap.get(method);
        // 我可以拿到返回值
        Object object = null;
        try {
            object = method.invoke(o, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //这一步是判断是否我们通过map添加了参数，如果添加了就放到request域中
        if (REQUESTPARAM.size() > 0) {
            Set<String> objects = REQUESTPARAM.keySet();
            Iterator<String> iterator = objects.iterator();
            if (iterator.hasNext()) {
                String key = iterator.next();
                request.setAttribute(key, REQUESTPARAM.get(key));
            }
        }
        REQUESTPARAM.clear();
        return object;
    }

    private Object[] getArgsAndBindParam(HttpServletRequest request, HttpServletResponse response, Method method) {
        // 当我们没有参数的时候直接返回就行了
        if (method.getParameterCount() == 0) {
            return null;
        }
        // 这里会拿到所有的标注了@Param注解的参数
        // 我们只要想通过参数拿简单值，就需要标注这个注解
        String[] methodParameterNames = ParameterNameUtils.getMethodParameterNamesByAnnotation(method);
        String value = null;
        Object[] args = new Object[method.getParameterCount()];
        int i = 0;
        // 设置标注了注解的参数
        for (String methodParameterName : methodParameterNames) {
            args[i] = request.getParameter(methodParameterName);
            i++;
        }
        // 封装request和response还有map
        // 拿到所有的参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        int j = 0;
        for (Class<?> parameterType : parameterTypes) {
            // 注入基本类型的属性
            if (DataBindingUtils.isCommonType(parameterType)) {
                if (parameterType.equals(Integer.class) || parameterType.equals(int.class)) {
                    args[j] = Integer.parseInt((String) args[j]);
                    j++;
                } else if (parameterType.equals(Double.class) || parameterType.equals(double.class)) {
                    args[j] = Double.parseDouble((String) args[j]);
                    j++;
                } else if (parameterType.equals(Long.class) || parameterType.equals(long.class)) {
                    args[j] = Long.parseLong((String) args[j]);
                    j++;
                }
            } else if (parameterType.equals(HttpServletRequest.class)) {  //注入request
                args[i] = request;
                i++;
            } else if (parameterType.equals(HttpServletResponse.class)) {  //注入response
                args[i] = response;
                i++;
            } else if (parameterType.equals(HttpSession.class)) {           //注入session
                args[i] = request.getSession();
                i++;
            } else if (parameterType.equals(Map.class)) {                   //注入map
                args[i] = REQUESTPARAM;
                i++;
            } else if (DataBindingUtils.isBean(parameterType)) {                     //如果是bean类型的话，自动注入
                args[i] = DataBindingUtils.autowriteParameter(request, parameterType);
                i++;
            }
        }
        return args;
    }

    private Method getRequestMappingMethod(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String servletPath = request.getServletPath();
        Method method = methodMap.get(servletPath);
        if (method == null) {
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
        return method;
    }

    /**
     * 根据当前servlet的路径扫描所有的servlet
     *
     * @param mainServletClass
     * @return
     */
    private static List<Class> scanWebModule(Class<?> mainServletClass) {


        String packageName = mainServletClass.getName().substring(0, mainServletClass.getName().lastIndexOf("."));
        Set<String> className = ClassUtils.getClassName(packageName, true);
        List<Class> classList = new ArrayList<>();
        for (String s : className) {
            if (packageName.equals(s)) {
                continue;
            }
            Class<?> aClass = null;
            try {
                aClass = Class.forName(s);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            classList.add(aClass);
        }

        return classList;
    }

    private static List<Class> scanAllModule() {

        Set<String> className = ClassUtils.getClassName("", true);
        List<Class> classList = new ArrayList<>();
        for (String s : className) {
            Class<?> aClass = null;
            try {
                aClass = Class.forName(s.substring(1));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            classList.add(aClass);
        }

        return classList;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
