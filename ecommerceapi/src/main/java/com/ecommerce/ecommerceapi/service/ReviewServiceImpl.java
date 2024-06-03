package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dao.ReviewRepository;
import com.ecommerce.ecommerceapi.dto.ReviewDTO;
import com.ecommerce.ecommerceapi.dto.UserDTO;
import com.ecommerce.ecommerceapi.entity.Product;
import com.ecommerce.ecommerceapi.entity.Review;
import com.ecommerce.ecommerceapi.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService{


    private ReviewRepository reviewRepository;

    private UserService userService;

    private ProductService productService;

    @Autowired
    public ReviewServiceImpl(ReviewRepository thereviewRepository, UserService theuserService, ProductService theproductService) {
        reviewRepository = thereviewRepository;
        userService = theuserService;
        productService = theproductService;
    }


    @Override
    public List<ReviewDTO> findReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream().map(ReviewDTO::new).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public Review createReview(ReviewDTO reviewDTO) {
        User user = userService.getUserEntityById(reviewDTO.getUserId());
        Product product = productService.getProductEntityById(reviewDTO.getProductId());

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setContent(reviewDTO.getContent());
        review.setRating(reviewDTO.getRating());
        review.setCreatedDate(LocalDateTime.now());

        return reviewRepository.save(review);
    }


    @Transactional
    public Review updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        review.setContent(reviewDTO.getContent());
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public String deleteReview(Long reviewId) {
        Review reviewToDelete = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found with ID: " + reviewId));
        reviewRepository.delete(reviewToDelete);
        return "Review with ID " + reviewId + " has been deleted successfully.";
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
    }
}
