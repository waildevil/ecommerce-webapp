package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.CategoryRepository;
import com.ecommerce.ecommerceapi.entity.Category;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository theCategoryRepository){
        categoryRepository = theCategoryRepository;
    }
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long theId) {
        Optional<Category> result = categoryRepository.findById(theId);

        Category theCategory = null;
        if(result.isPresent()){
            theCategory = result.get();
        }
        else{
            throw new RuntimeException("Did not find category id - " + theId);
        }
        return theCategory;
    }

    @Override
    public Category save(Category theCategory) {
        return categoryRepository.save(theCategory);
    }

    @Override
    public void deleteById(Long theId) {
        categoryRepository.deleteById(theId);
    }

    @Override
    public Category updateCategory(Long theId, Category theCategory) {
        Category categoryToUpdate = categoryRepository.findById(theId).
                orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + theId));
        categoryToUpdate.setName(theCategory.getName());
        categoryToUpdate.setDescription(theCategory.getDescription());

        return categoryRepository.save(categoryToUpdate);
    }
}
