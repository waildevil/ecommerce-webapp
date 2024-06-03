import React from 'react';
//import './Order.css'; // Your CSS file for styling

const Order = ({ order }) => {
  return (
    <div className="order">
      <div className="order-header">
        <div>
          <span>ORDER PLACED</span>
          <span>{new Date(order.orderDate).toLocaleDateString()}</span>
        </div>
        <div>
          <span>TOTAL</span>
          <span>{order.totalPrice}€</span>
        </div>
        <div>
          <span>DISPATCH TO</span>
          <span>{order.user.name}</span>
        </div>
        <div>
          <button className="order-details-button">View order details</button>
        </div>
      </div>
      <div className="order-body">
        {/* Map through order items and display them */}
      </div>
    </div>
  );
};

export default Order;
