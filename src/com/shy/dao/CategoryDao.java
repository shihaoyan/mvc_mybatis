package com.shy.dao;

import com.shy.annotation.*;
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
 * @create 2020-06-18 13:53
 * 描述：
 */
@Dao
public interface CategoryDao {
    /**
     * 查询所有的分类信息
     *
     * @return
     */
    @Select("select * from category")
    List<Category> categoryList();


    @Insert("insert into category(cname) values( #{cname} )")
    void add(Category category);

    @Delete("delete from category where cid = #{cid} ")
    void delete(@Param("cid") Integer cid);

    @Update("update category set cname = #{cname} where cid = #{cid}")
    void update(Category category);

    @Select("select * from category where cid = #{cid}")
    Category categoryFindById(@Param("cid") Integer cid);
}
