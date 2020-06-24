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

    @Override
    public void add(Category category) {
        categoryDao.add(category);
    }

    @Override
    public void delete(Integer cid) {
        categoryDao.delete(cid);
        // 接下来应该删除分类下的所有商品
    }

    @Override
    public void update(Category category) {
        categoryDao.update(category);
    }

    @Override
    public Category categoryFindById(Integer cid) {
        return categoryDao.categoryFindById(cid);
    }
}
