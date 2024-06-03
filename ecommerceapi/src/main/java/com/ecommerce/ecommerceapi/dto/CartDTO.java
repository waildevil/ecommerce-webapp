package com.ecommerce.ecommerceapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    private Long id;
    private List<CartItemDTO> items;
    private Double totalPrice;
    private Integer totalQuantity;
}
