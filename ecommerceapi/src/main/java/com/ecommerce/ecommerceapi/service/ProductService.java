package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dto.ProductDTO;
import com.ecommerce.ecommerceapi.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductDTO> findAll();
    List<ProductDTO> findAllActive();
    ProductDTO findById(Long theId);
    Product createProduct(ProductDTO productDTO);
    boolean deleteById(Long theId);
    Product updateProduct(Long productId, ProductDTO productDTO);
    Product getProductEntityById(Long productId);
    void updateProductImage(Long productId, String imageUrl);
    List<ProductDTO> searchProductsByNameAndCategory(String name, Optional<Long> categoryId);
    List<ProductDTO> searchProductsByNameAndCategory(String searchTerm, Optional<Long> categoryId, boolean isAdmin);

}
