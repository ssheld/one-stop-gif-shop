package com.ssheld.onestopgifshop.service;

import com.ssheld.onestopgifshop.dao.CategoryDao;
import com.ssheld.onestopgifshop.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public Category findById(Long id) {
        return categoryDao.findById(id);
    }

    public void save(Category category) {
        categoryDao.save(category);
    }

    public void delete(Category category) {
        categoryDao.delete(category);
    }
}
