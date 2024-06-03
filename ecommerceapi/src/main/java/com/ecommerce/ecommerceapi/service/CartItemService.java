package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.entity.CartItem;

public interface CartItemService {

    CartItem addCartItem(Long userId, Long productId, int quantity);
    CartItem updateCartItem(Long cartItemId, int quantity);
    void removeCartItem(Long cartItemId);
}
