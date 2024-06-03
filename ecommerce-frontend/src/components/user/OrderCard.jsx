import React from 'react';
import './OrderCard.css'
import { Link } from 'react-router-dom';
import Navigbar from '../Homepage/Navigbar';

function OrderCard({ order }) {
  if (!order) {
      return <div>Loading order details...</div>;
  }


  const { date, status, totalPrice, orderDetails } = order;

  return (
    <>
    <div className="order-card">
          <div className="order-header">
              <h3>Order #{order.id}</h3>
              <h4>Order placed on: {new Date(date).toLocaleDateString()}</h4>
              <h4>Status: {status}</h4>
              <p>Total Price: {totalPrice.toFixed(2)}€</p>
          </div>

          <div className="order-details">

              
            {orderDetails.map((detail) => (
                        <div key={detail.id} className="product-detail">
                            <img src={`http://localhost:8080${detail.product.imageUrl}`} alt={detail.product.name} />
                            <div>
                                <h5 className='name'>
                                    <Link to={`/products/${detail.product.id}`}>
                                    {detail.product.name}
                                    </Link>
                                </h5>
                                <p>Quantity: {detail.quantity}</p>
                                <p>Price: {detail.product.price.toFixed(2)}€</p>
                                <Link to={`/reviews/${detail.product.id}`}>Write a review</Link>
                            </div>
                        </div>
                    ))}
          </div>
    </div>
    
    </>
  );
}

export default OrderCard;
