package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dto.CartDTO;
import com.ecommerce.ecommerceapi.entity.Address;
import com.ecommerce.ecommerceapi.entity.Cart;
import com.ecommerce.ecommerceapi.entity.Product;
import com.ecommerce.ecommerceapi.entity.User;

public interface CartService {

    CartDTO getCartByUserId(Long userId);
    Cart createCartForUser(User user);
    Cart findById(Long theId);
    void clearCart(Long userId);
    Cart getCartEntityByUserId(Long userId);

}
