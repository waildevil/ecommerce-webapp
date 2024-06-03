package com.ecommerce.ecommerceapi.dto;

import com.ecommerce.ecommerceapi.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private Long id;
    private Long userId;
    private Long productId;
    private String content;
    private String username;
    private Double rating;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createdDate;


    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.content = review.getContent();
        if (review.getUser() != null) {
            this.userId = review.getUser().getId();
            this.username = review.getUser().getUsername();
        } else {
            this.userId = null;
            this.username = null;
        }
        this.productId = review.getProduct() != null ? review.getProduct().getId() : null;
        this.rating = review.getRating();
        this.createdDate = review.getCreatedDate();
    }

}
