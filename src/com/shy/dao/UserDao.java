package com.shy.dao;

import com.shy.annotation.Dao;
import com.shy.annotation.Param;
import com.shy.annotation.Select;
import com.shy.annotation.Update;
import com.shy.beans.User;

import java.util.List;

/**
 * @author 石皓岩
 * @create 2020-06-18 13:14
 * 描述：
 */
@Dao
public interface UserDao {

    /**
     * 查询所有用户
     *
     * @return
     */
    @Select("select * from user")
    List<User> findAllUser();

    /**
     * 更新用户状态
     *
     * @param uid
     * @param state
     */
    @Update("update user set state = #{state} where uid = #{uid}")
    void updateState(@Param("uid") Integer uid, @Param("state") Integer state);

    /**
     * 通过id查询用户
     *
     * @param uid
     * @return
     */
    @Select("select * from user where uid = #{uid}")
    User findUserById(@Param("uid") Integer uid);

    /**
     * 更新用户，根据用户id
     * @param user
     */
    @Update("update user " +
            "set account = #{account},username = #{username}, password = #{password},sex = #{sex},phone = #{phone} " +
            "where uid = #{uid}")
    void update(User user);

}
