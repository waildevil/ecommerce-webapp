package com.ecommerce.ecommerceapi.rest;

import com.ecommerce.ecommerceapi.dto.AddItemRequest;
import com.ecommerce.ecommerceapi.entity.CartItem;
import com.ecommerce.ecommerceapi.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cart/items")
public class CartItemRestController {

    @Autowired
    private CartItemService cartItemService;


    @PostMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest request) {
        CartItem cartItem = cartItemService.addCartItem(request.getUserId(), request.getProductId(), request.getQuantity());
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }


    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long cartItemId, @RequestParam int quantity) {
        CartItem updatedCartItem = cartItemService.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(updatedCartItem);
    }


    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long cartItemId) {
        cartItemService.removeCartItem(cartItemId);
        return ResponseEntity.ok().build();
    }
}
