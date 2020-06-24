package com.shy.servlet;

import com.shy.annotation.*;
import com.shy.beans.Admin;
import com.shy.beans.Category;
import com.shy.beans.User;
import com.shy.service.AdminService;
import com.shy.service.CategoryService;
import com.shy.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 石皓岩
 * @create 2020-06-18 12:09
 * 描述：
 */
@WebModule
@ReturnPage(prefix = "/admin/")
public class AdminModule {

    @AutoInstantiate
    private AdminService adminService;
    @AutoInstantiate
    private UserService userService;
    @AutoInstantiate
    private CategoryService categoryService;

    @RequestMapping("/admin/login")
    public String login(@Param("account") String account,
                        @Param("password") String password,
                        HttpServletRequest request) {
        Admin admin = adminService.login(account, password);
        if (admin != null) {
            request.getSession().setAttribute("admin", admin);
            return "home";
        }
        return "index";
    }

    @RequestMapping("/admin/user/list")
    @ReturnPage(prefix = "/admin/user/")
    public String userList(Map<String, Object> map) {

        List<User> userList = userService.findAllUser();
        map.put("userList", userList);

        return "list";
    }

    @RequestMapping("/admin/user/updateState")
    public String updateState(@Param("uid") Integer uid,
                              @Param("state") Integer state) {

        userService.updateState(uid, state);

        return "forward:/admin/user/list";
    }

    @RequestMapping("/admin/category/list")
    @ReturnPage(prefix = "/admin/category/")
    public String categoryList(Map<String, Object> map) {

        List<Category> categories = categoryService.categoryList();
        map.put("categoryList", categories);

        return "list";
    }

    @RequestMapping("/admin/user/edit")
    @ReturnPage(prefix = "/admin/user/")
    public String userEdit(@Param("uid") Integer uid,
                           Map<String, Object> map) {

        User user = userService.findUserById(uid);
        map.put("user", user);

        return "edit";
    }

    @RequestMapping("/user/admin/update")
    public String userUpdate(User user) {

        userService.update(user);


        return "forward:/admin/user/list";
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<Admin> list(){

        List<Admin> list = new ArrayList<>();
        list.add(new Admin(1,"aaa","aaa"));
        list.add(new Admin(2,"bbb","bbb"));
        list.add(new Admin(3,"ccc","ccc"));
        list.add(new Admin(4,"ddd","ddd"));
        list.add(new Admin(5,"eee","eee"));
        list.add(new Admin(6,"fff","fff"));

        return list;
    }


}
