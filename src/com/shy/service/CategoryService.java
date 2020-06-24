package com.shy.service;

import com.shy.beans.Category;

import java.util.List;

/**
 * @author 石皓岩
 * @create 2020-06-18 13:52
 * 描述：
 */
public interface CategoryService {
    List<Category> categoryList();

    void add(Category category);

    void delete(Integer cid);

    void update(Category category);

    Category categoryFindById(Integer cid);
}
