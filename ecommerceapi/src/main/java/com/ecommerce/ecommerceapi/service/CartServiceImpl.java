package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.CartItemRepository;
import com.ecommerce.ecommerceapi.dao.CartRepository;
import com.ecommerce.ecommerceapi.dao.ProductRepository;
import com.ecommerce.ecommerceapi.dao.UserRepository;
import com.ecommerce.ecommerceapi.dto.CartDTO;
import com.ecommerce.ecommerceapi.dto.CartItemDTO;
import com.ecommerce.ecommerceapi.dto.ProductDTO;
import com.ecommerce.ecommerceapi.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Cart findById(Long theId) {
        Optional<Cart> result = cartRepository.findById(theId);

        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Did not find Cart id - " + theId);
        }
    }

    @Override
    public CartDTO getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(user));

        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());

        List<CartItemDTO> cartItemDTOs = cart.getItems().stream().map(item -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(item.getProduct().getId());
            productDTO.setName(item.getProduct().getName());
            productDTO.setDescription(item.getProduct().getDescription());
            productDTO.setPrice(item.getProduct().getPrice());
            productDTO.setImageUrl(item.getProduct().getImageUrl());
            productDTO.setActive(item.getProduct().isActive());
            productDTO.setQuantity(item.getQuantity());

            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(item.getId());
            cartItemDTO.setProduct(productDTO);
            cartItemDTO.setQuantity(item.getQuantity());
            cartItemDTO.setSubtotal(item.getProduct().getPrice() * item.getQuantity());
            return cartItemDTO;
        }).collect(Collectors.toList());

        double totalPrice = cartItemDTOs.stream().mapToDouble(CartItemDTO::getSubtotal).sum();
        int totalQuantity = cartItemDTOs.stream().mapToInt(CartItemDTO::getQuantity).sum();

        cartDTO.setItems(cartItemDTOs);
        cartDTO.setTotalPrice(totalPrice);
        cartDTO.setTotalQuantity(totalQuantity);

        return cartDTO;
    }

    @Override
    public Cart createCartForUser(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart = getCartEntityByUserId(userId);
        List<CartItem> items = cart.getItems();
        if (!items.isEmpty()) {
            items.clear();
            cartRepository.save(cart);
        }
    }

    public Cart getCartEntityByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user id: " + userId));
    }


}
