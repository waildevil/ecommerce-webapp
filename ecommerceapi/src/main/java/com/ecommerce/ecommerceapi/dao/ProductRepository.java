package com.ecommerce.ecommerceapi.dao;

import com.ecommerce.ecommerceapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContainingIgnoreCase(String searchTerm);
    List<Product> findByNameContainingIgnoreCaseAndCategoryId(String searchTerm, Long categoryId);
    List<Product> findByIsActiveTrue();

    List<Product> findByNameContainingIgnoreCaseAndCategoryIdAndIsActiveTrue(String name, Long categoryId);
    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategories();

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.isActive = true")
    List<Product> findAllActiveWithCategories();
}
