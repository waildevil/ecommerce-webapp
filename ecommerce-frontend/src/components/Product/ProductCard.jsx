import React from 'react';
import "./ProductCard.css";
import { Link } from 'react-router-dom';


function ProductCard({ product }) {
    if (!product) {
        return <div>Product information is not available.</div>;
      }
 
    const imageUrl = `http://localhost:8080${product.imageUrl}`;

   
    

    return (
        <Link to={`/products/${product.id}`} className="card-link">
        <div className="card">
          <img src={imageUrl} alt={product.name} />
          <div className="card-content">
            <h5 className="card-title">{product.name}</h5>
            <div className="rating-and-quantity">
              <div className="quantity">{product.quantity} in stock</div>
            </div>
            <div className="card-footer">
              <p className="card-price">{product.price}€</p>
            </div>
          </div>
        </div>
      </Link>
    );
}

export default ProductCard;