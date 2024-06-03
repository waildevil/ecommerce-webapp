package com.ecommerce.ecommerceapi.dto;

import com.ecommerce.ecommerceapi.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private double price;
    private double quantity;
    private CategoryDTO category;
    private Double averageRating;
    private boolean isActive;
    private String imageUrl;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.category = product.getCategory() != null ? new CategoryDTO(product.getCategory().getId(), product.getCategory().getName()) : null;
        this.isActive = product.isActive();
        this.imageUrl = product.getImageUrl();
    }

    public ProductDTO(Long id) {
        this.id = id;
    }


}
