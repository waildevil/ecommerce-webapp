package com.ecommerce.ecommerceapi.dto;

import com.ecommerce.ecommerceapi.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {


    private Long id;
    private int quantity;
    private ProductDTO product;


    public OrderDetailDTO(OrderDetail orderDetail) {
        this.id = orderDetail.getId();
        this.quantity = orderDetail.getQuantity();
        this.product = new ProductDTO(orderDetail.getProduct()); // Assuming ProductDTO constructor takes a Product

        System.out.println("Product in DTO: " + this.product.getName());
    }
}
