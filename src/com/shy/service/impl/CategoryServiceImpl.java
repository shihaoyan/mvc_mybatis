package com.shy.service.impl;

import com.shy.annotation.AutoInstantiate;
import com.shy.annotation.Service;
import com.shy.beans.Category;
import com.shy.dao.CategoryDao;
import com.shy.service.CategoryService;

import java.util.List;

/**
 * @author 石皓岩
 * @create 2020-06-18 13:58
 * 描述：
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @AutoInstantiate
    private CategoryDao categoryDao;
    @Override
    public List<Category> categoryList() {
        return categoryDao.categoryList();
    }
}
