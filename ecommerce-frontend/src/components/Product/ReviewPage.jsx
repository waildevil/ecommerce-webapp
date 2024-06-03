import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './ReviewPage.css'; 
import StarRatings from 'react-star-ratings';
import Navigbar from '../Homepage/Navigbar';
import AuthService from '../../services/AuthService'; 

function ReviewPage() {
  const [content, setContent] = useState('');
  const [rating, setRating] = useState(0);
  const [product, setProduct] = useState(null); 
  const { productId } = useParams(); 
  const navigate = useNavigate(); 

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/products/${productId}`);
        setProduct(response.data);
      } catch (error) {
        console.error('Error fetching product:', error);
      }
    };

    fetchProduct();
  }, [productId]);

  const handleReviewChange = (e) => {
    setContent(e.target.value);
  };

  const changeRating = (newRating) => {
    setRating(newRating);
  };

  const submitReview = async (e) => {
    e.preventDefault();


    const currentUser = AuthService.getCurrentUser();
    if (!currentUser) {
      alert('You need to be logged in to submit a review.');
      return;
    }

    const userId = currentUser.userId;

    try {
      if (!content) {
        alert('Please enter a review.');
        return;
      }
      await axios.post('http://localhost:8080/api/reviews/createReview', {
        userId: userId,
        productId: productId,
        content: content,
        rating: rating
      });
      alert('Review submitted successfully!');
      navigate('/orders');
    } catch (error) {
      console.error('Error submitting review:', error);
      alert('Failed to submit review.');
    }
  };

  return (
    <>
      <Navigbar />
      <div className="review-page-container">
        <h1>Write a Review</h1>
        {product && (
          <div className="product-info">
            <img src={`http://localhost:8080${product.imageUrl}`} alt={product.name} className="product-image" />
            <h2 className="product-title">{product.name}</h2>
          </div>
        )}

        <StarRatings
          rating={rating}
          starRatedColor="gold"
          changeRating={changeRating}
          numberOfStars={5}
          name='rating'
          className="star-ratings"
        />

        <form onSubmit={submitReview}>
          <textarea
            className="review-textarea"
            onChange={handleReviewChange}
            value={content}
            placeholder="Write your review here..."
          />
          <button type="submit" className="review-submit-button">Submit Review</button>
        </form>
      </div>
    </>
  );
}

export default ReviewPage;
