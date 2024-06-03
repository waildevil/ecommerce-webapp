package com.ecommerce.ecommerceapi.dao;

import com.ecommerce.ecommerceapi.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
