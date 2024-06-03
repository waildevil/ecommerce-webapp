package com.ecommerce.ecommerceapi.rest;

import com.ecommerce.ecommerceapi.entity.Category;
import com.ecommerce.ecommerceapi.entity.Product;
import com.ecommerce.ecommerceapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/categories")
public class CategoryRestController {

    private CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService theCategoryService){
        categoryService = theCategoryService;
    }


    @GetMapping("")
    public List<Category> findAll(){
        return categoryService.findAll();
    }


    @GetMapping("/{categoryId}")
    public Category getCategory(@PathVariable Long categoryId){
        Category theCategory = categoryService.findById(categoryId);
        if(theCategory == null){
            throw new RuntimeException("Category id not found - " + categoryId);
        }
        return theCategory;
    }


    @PostMapping("/createCategory")
    public Category createCategory(@RequestBody Category theCategory){
        theCategory.setId(0L);
        Category dbCategory = categoryService.save(theCategory);
        return dbCategory;
    }


    @PutMapping("/updateCategory/{id}")
    public Category updateCategory(@PathVariable Long id,@RequestBody Category theCategory){
        Category dbCategory = categoryService.updateCategory(id, theCategory);
        return dbCategory;
    }


    @DeleteMapping("/deleteCategory/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){

        Category tempCategory = categoryService.findById(categoryId);
        if(tempCategory == null){
            throw new RuntimeException("Category id not found - " + categoryId);
        }
        categoryService.deleteById(categoryId);
        return "Deleted category id - " + categoryId;
    }
}
