package com.ecommerce.ecommerceapi.rest;


import com.ecommerce.ecommerceapi.config.JwtService;
import com.ecommerce.ecommerceapi.dao.ReviewRepository;
import com.ecommerce.ecommerceapi.dto.ProductDTO;
import com.ecommerce.ecommerceapi.dto.ReviewDTO;
import com.ecommerce.ecommerceapi.entity.Product;
import com.ecommerce.ecommerceapi.entity.Review;
import com.ecommerce.ecommerceapi.service.ImageStorageService;
import com.ecommerce.ecommerceapi.service.ProductService;
import com.ecommerce.ecommerceapi.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")

public class ProductRestController {

    private ProductService productService;
    private ReviewService reviewService;
    private ImageStorageService imageStorageService;
    private JwtService jwtService;
    private ReviewRepository reviewRepository;

    @Autowired
    public ProductRestController(ProductService theProductService, ReviewService theReviewService,
                                 ImageStorageService theImageStorageService, JwtService theJwtService) {
        productService = theProductService;
        imageStorageService = theImageStorageService;
        jwtService = theJwtService;
        reviewService = theReviewService;
    }


    @GetMapping
    public List<ProductDTO> getProducts(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String role = jwtService.extractRole(token);
            if (role.equals("ROLE_ADMIN")) {
                return productService.findAll();
            }
        }
        return productService.findAllActive();
    }



    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO productDTO = productService.findById(productId);
        if (productDTO == null) {
            throw new RuntimeException("Product id not found - " + productId);
        }
        return ResponseEntity.ok(productDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            Product createdProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(createdProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error creating product: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        try {
            Product updatedProduct = productService.updateProduct(productId, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }


    @GetMapping("/{productId}/reviews")
    public List<ReviewDTO> getProductReviews(@PathVariable Long productId) {
        return reviewService.findReviewsByProductId(productId);
    }



    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        boolean isDeleted = productService.deleteById(productId);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Product marked as inactive - id: " + productId);
    }

    @PostMapping("/{productId}/uploadImage")
    public ResponseEntity<String> uploadProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile file) throws IOException {
        String imageUrl = imageStorageService.storeImage(file);
        productService.updateProductImage(productId, imageUrl);
        return ResponseEntity.ok("Image uploaded successfully: " + imageUrl);
    }



    @GetMapping("/search")
    public List<ProductDTO> searchProducts(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                           @RequestParam String searchTerm,
                                           @RequestParam(required = false) Long categoryId) {
        boolean isAdmin = false;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String role = jwtService.extractRole(token);
            isAdmin = role.equals("ADMIN");
        }
        return productService.searchProductsByNameAndCategory(searchTerm, Optional.ofNullable(categoryId), isAdmin);
    }

}





