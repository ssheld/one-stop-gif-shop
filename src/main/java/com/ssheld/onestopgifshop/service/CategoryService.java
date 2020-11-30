package com.ssheld.onestopgifshop.service;

import com.ssheld.onestopgifshop.model.Category;

import java.util.List;

/**
 * Author: Stephen Sheldon
 **/
public interface CategoryService {
    List<Category> findAll();
    Category findById(Long id);
    void save(Category category);
    void delete(Category category);
}
