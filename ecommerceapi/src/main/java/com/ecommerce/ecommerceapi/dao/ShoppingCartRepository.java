package com.ecommerce.ecommerceapi.dao;

import com.ecommerce.ecommerceapi.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<Cart,Long> {
}
