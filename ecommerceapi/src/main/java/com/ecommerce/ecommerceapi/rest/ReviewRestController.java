package com.ecommerce.ecommerceapi.rest;

import com.ecommerce.ecommerceapi.dto.ReviewDTO;
import com.ecommerce.ecommerceapi.entity.Review;
import com.ecommerce.ecommerceapi.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/createReview")
    public ResponseEntity<?> createReview(@RequestBody ReviewDTO reviewDTO) {
        Review review = reviewService.createReview(reviewDTO);
        return ResponseEntity.ok(review);
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/updateReview/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @RequestBody ReviewDTO reviewDTO) {
        Review updatedReview = reviewService.updateReview(reviewId, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        String message = "Review with ID " + reviewId + " has been deleted successfully.";
        return ResponseEntity.ok(message);
    }

    // Endpoint to get a review
    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }



}
