package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.CategoryRepository;
import com.ecommerce.ecommerceapi.dao.ProductRepository;
import com.ecommerce.ecommerceapi.dao.ReviewRepository;
import com.ecommerce.ecommerceapi.dto.ProductDTO;
import com.ecommerce.ecommerceapi.dto.UserDTO;
import com.ecommerce.ecommerceapi.entity.Category;
import com.ecommerce.ecommerceapi.entity.Product;
import com.ecommerce.ecommerceapi.entity.Review;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{


    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ReviewRepository reviewRepository;


    @Autowired
    public ProductServiceImpl(ProductRepository theProductRepository,
                              CategoryRepository theCategoryRepository,
                              ReviewRepository theReviewRepository){

        productRepository = theProductRepository;
        categoryRepository = theCategoryRepository;
        reviewRepository = theReviewRepository;
    }

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAllWithCategories();
        return products.stream()
                .map(this::convertToDTOWithAverageRating)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> findAllActive() {
        List<Product> products = productRepository.findAllActiveWithCategories();
        return products.stream()
                .map(this::convertToDTOWithAverageRating)
                .collect(Collectors.toList());
    }


    private ProductDTO convertToDTOWithAverageRating(Product product) {
        ProductDTO productDTO = new ProductDTO(product);
        Double averageRating = reviewRepository.findByProductId(product.getId())
                .stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
        productDTO.setAverageRating(averageRating);
        return productDTO;
    }

    public Product getProductEntityById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }

    public ProductDTO findById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        ProductDTO productDTO = new ProductDTO(product);


        Double averageRating = reviewRepository.findByProductId(productId)
                .stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        productDTO.setAverageRating(averageRating);

        return productDTO;
    }



    @Override
    public Product createProduct(ProductDTO productDTO) {


        Long categoryId = productDTO.getCategory().getId();
        if (productDTO.getCategory() == null || productDTO.getCategory().getId() == null) {
            throw new IllegalArgumentException("Category is required");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setCategory(category);
        product.setActive(productDTO.isActive());

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, ProductDTO productDTO) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (productDTO.getCategory() != null && productDTO.getCategory().getId() != null) {
            if (existingProduct.getCategory() == null || !existingProduct.getCategory().getId().equals(productDTO.getCategory().getId())) {
                Category category = categoryRepository.findById(productDTO.getCategory().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + productDTO.getCategory().getId()));
                existingProduct.setCategory(category);
            }
        }

        if (existingProduct.isActive() != productDTO.isActive()) {
            existingProduct.setActive(productDTO.isActive());
        }


        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());

        return productRepository.save(existingProduct);
    }

    @Override
    public boolean deleteById(Long productId) {

        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product existingProduct = product.get();
            existingProduct.setActive(false);
            productRepository.save(existingProduct);
            return true;
        }
        return false;
    }

    public void updateProductImage(Long productId, String imageUrl) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        product.setImageUrl(imageUrl);
        productRepository.save(product);
    }

    public List<ProductDTO> searchProductsByNameAndCategory(String searchTerm, Optional<Long> categoryId) {
        List<Product> products;
        if (categoryId.isPresent()) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategoryId(searchTerm, categoryId.get());
        } else {
            products = productRepository.findByNameContainingIgnoreCase(searchTerm);
        }
        return products.stream().map(ProductDTO::new).collect(Collectors.toList());

    }

    public List<ProductDTO> searchProductsByNameAndCategory(String searchTerm, Optional<Long> categoryId, boolean isAdmin) {
        List<Product> products;
        if (categoryId.isPresent()) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategoryId(searchTerm, categoryId.get());
        } else {
            products = productRepository.findByNameContainingIgnoreCase(searchTerm);
        }

        if (!isAdmin) {
            products = products.stream().filter(Product::isActive).collect(Collectors.toList());
        }

        return products.stream().map(this::convertToDTOWithAverageRating).collect(Collectors.toList());
    }
}
