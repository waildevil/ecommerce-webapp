import React, { useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import "./ProductDetailPage.css"
import { useState, useEffect } from "react";
import { getProduct, getProductReviews } from '../../services/ProductService';
import Navigbar from '../Homepage/Navigbar';
import axios from "axios";
import AuthService from '../../services/AuthService';

function ProductDetailPage() {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [quantity, setQuantity] = useState(1);
    const navigate = useNavigate();
    const paypalRef = useRef();

    const user = AuthService.getCurrentUser();

    useEffect(() => {
        setIsLoading(true);

        getProduct(productId)
            .then(response => {
                setProduct(response.data);
      
                return getProductReviews(productId);
            })
            .then(reviewsResponse => {
                setReviews(reviewsResponse.data);
            })
            .catch(error => {
                console.error("Fetching product details or reviews failed: ", error);
                setError(error);
            })
            .finally(() => {
                setIsLoading(false);
            });
    }, [productId]);

    const renderStars = (rating) => {
        return [...Array(5)].map((_, index) => (
            <span key={index} className={`star ${index < Math.round(rating) ? 'filled' : 'empty'}`}>
                ★
            </span>
        ));
    };

    const renderReviews = () => {
        return reviews.length > 0 ? (
            reviews.map((review) => {
    
                const reviewDate = new Date(review.createdDate);

        
                const formattedDate = reviewDate.toLocaleDateString("en-US", {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                    hour: '2-digit',
                    minute: '2-digit',
                });

                return (
                    <div key={review.id} className="review">
                        <div className="review-title">{review.username} - {formattedDate}</div>
                        <div className="review-body">{review.content}</div>
                    </div>
                );
            })
        ) : (
            <p>No reviews available.</p>
        );
    };

    const handleAddToCart = async () => {
        try {
            const orderPayload = {
                userId: user.userId,
                productId: product.id,
                quantity: quantity
            };

            console.log('Sending payload to server:', orderPayload);

            const response = await axios.post('http://localhost:8080/api/cart/items/add', orderPayload, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${user.token}`
                }
            });

            console.log('Item added to cart:', response.data);

            const itemSubtotal = product.price * quantity;

            navigate('/added-to-basket', {
                state: {
                    item: product,
                    subtotal: itemSubtotal,
                    totalItems: quantity
                }
            });

        } catch (error) {
            console.error('Error adding item to cart:', error);
        }
    };

    const handleSingleProductCheckout = async (productId, quantity, sum, jwt) => {
        try {
            const params = new URLSearchParams({
                sum: sum,
                productId: productId,
                quantity: quantity,
                jwt: jwt
            });
    
            const response = await axios.post(`http://localhost:8080/api/paypal/pay?${params.toString()}`, null, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${jwt}`
                }
            });
    
            const approvalUrl = response.data;
            console.log('Redirecting to PayPal approval URL:', approvalUrl);
            window.open(approvalUrl, '_blank');
        } catch (error) {
            console.error('Error processing single product checkout:', error);
            if (error.response) {
                console.error('Response data:', error.response.data);
                console.error('Response status:', error.response.status);
                console.error('Response headers:', error.response.headers);
            } else if (error.request) {
                console.error('Request data:', error.request);
            } else {
                console.error('Error message:', error.message);
            }
        }
    };
    
    const handleBuyNow = async () => {
        if (!user) {
            console.error('User not logged in');
            navigate('/login'); 
            return;
        }

        try {
            const sum = product.price * quantity; 
            if (isNaN(sum)) {
                throw new Error(`Invalid total price: ${sum}`);
            }

            await handleSingleProductCheckout(product.id, quantity, sum, user.token);

        } catch (error) {
            console.error('Error processing payment:', error);
            if (error.response) {
                console.error('Response data:', error.response.data);
                console.error('Response status:', error.response.status);
                console.error('Response headers:', error.response.headers);
            } else if (error.request) {
                console.error('Request data:', error.request);
            } else {
                console.error('Error message:', error.message);
            }
        }
    };

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>Error fetching product details. Please try again later.</div>;
    }

    if (!product) {
        return <div>Product not found.</div>;
    }

    return (
        <>
            <script
                src="https://www.paypal.com/sdk/js?client-id=test&buyer- 
                country=US&currency=USD&components=buttons&enable-funding=venmo"
                data-sdk-integration-source="developer-studio"
            ></script>
            <Navigbar />
            <div className="product-detail-page">
                <div className="product-image">
                    <img src={`http://localhost:8080${product.imageUrl}`} alt={product.name} />
                </div>
                <div className="product-main-content">
                    <div className="product-details">
                        <h1>{product.name}</h1>
                        <p>{product.description}</p>
                        <div className="product-rating">{renderStars(product.averageRating)}</div>
                        <div className="product-quantity">Quantity: {product.quantity}</div>
                        <div className="product-price">Price: {product.price}€</div>
                        <div className="product-purchase-options">
                            <label htmlFor="quantity">Quantity:</label>
                            <select id="quantity" value={quantity} onChange={(e) => setQuantity(e.target.value)}>
                                {[...Array(product.quantity)].map((_, i) => (
                                    <option key={i} value={i + 1}>{i + 1}</option>
                                ))}
                            </select>
                            <button onClick={handleAddToCart} className="add-to-basket-btn">Add to Basket</button>
                            <button onClick={handleBuyNow} className="buy-now-btn">
                                Buy with Paypal
                            </button>
                        </div>
                    </div>
                    <div className="product-reviews">
                        <h3>Reviews:</h3>
                        {renderReviews()}
                    </div>
                </div>
            </div>
        </>
    );
}

export default ProductDetailPage;
