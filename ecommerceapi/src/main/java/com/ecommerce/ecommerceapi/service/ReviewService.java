package com.ecommerce.ecommerceapi.service;

import com.ecommerce.ecommerceapi.dto.ReviewDTO;
import com.ecommerce.ecommerceapi.entity.Review;

import java.util.List;

public interface ReviewService {

    Review createReview(ReviewDTO reviewDTO);
    Review updateReview(Long reviewId, ReviewDTO reviewDTO);
    String deleteReview(Long reviewId);
    Review getReview(Long reviewId);
    List<ReviewDTO> findReviewsByProductId(Long productId);

}
