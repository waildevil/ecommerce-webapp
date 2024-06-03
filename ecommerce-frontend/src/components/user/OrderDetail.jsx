// OrderDetail.jsx
import React from 'react';

const OrderDetail = ({ order }) => {
  return (
    <div className="order-detail-card">
    <h2>Order Detail</h2>
    <p>Date: {new Date(order.date).toLocaleString()}</p>
    <p>Status: {order.status}</p>
    <p>Total Price: €{order.totalPrice.toFixed(2)}</p>
    <h3>Items:</h3>
    {order.orderDetails.map((item, index) => (
      <div key={index} className="order-item">
        <p>Product ID: {item.productId}</p>
        <p>Quantity: {item.quantity}</p>
      </div>
    ))}
  </div>
  );
};

export default OrderDetail;
