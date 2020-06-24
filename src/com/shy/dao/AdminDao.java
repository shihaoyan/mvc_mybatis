package com.shy.dao;

import com.shy.annotation.Dao;
import com.shy.annotation.Param;
import com.shy.annotation.Select;
import com.shy.beans.Admin;
import com.shy.beans.Category;
import com.shy.myenum.DataType;
import com.shy.utils.JDBCUtils;
import com.shy.utils.DataBindingUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * @author 石皓岩
 * @create 2020-06-18 12:18
 * 描述：
 */
@Dao
public interface AdminDao {

    @Select("select * from admin where account = #{account} and password = #{password}")
    Admin login(@Param("account") String account, @Param("password") String password);

}
