package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.entity.Category;


import java.util.List;

public interface CategoryService {

    List<Category> findAll();
    Category findById(Long theId);
    Category save(Category theCategory);
    void deleteById(Long theId);
    Category updateCategory(Long theId, Category theCategory);
}
