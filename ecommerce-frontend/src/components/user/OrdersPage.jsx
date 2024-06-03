import React, { useState, useEffect } from 'react';
import OrderCard from './OrderCard';
import axios from 'axios';
import './OrdersPage.css'; // Make sure you have this CSS file for styling
import Navigbar from '../Homepage/Navigbar';
import AuthService from '../../services/AuthService';

function OrdersPage() {
  const [orders, setOrders] = useState([]); // Initialize as an empty array
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const user = AuthService.getCurrentUser();
    console.log('Retrieved user from local storage:', user);
    if (!user) {
      setError('No logged-in user found');
      setLoading(false);
      return;
    }

    const fetchOrders = async () => {
      try {
        console.log('Fetching orders for user ID:', user.userId);
        const response = await axios.get(`http://localhost:8080/api/users/${user.userId}`, {
          headers: {
            Authorization: `Bearer ${user.token}`
          }
        });
        const sortedOrders = response.data.orders.sort((a, b) => new Date(b.date) - new Date(a.date));
        console.log('Fetched orders:', response.data);
        setOrders(sortedOrders);
      } catch (error) {
        console.error('Error fetching orders:', error);
        setError('Failed to fetch orders');
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);



  

  if (loading) return <div>Loading orders...</div>;
  if (error) return <div>{error}</div>;

  return (
    <>
      <Navigbar />
      <div className="orders-page">
        <h1 style={{ marginTop: '20px', marginBottom: '30px' }}>Your Orders</h1>
        {orders.length > 0 ? (
          orders.map(order => (
            <OrderCard key={order.id} order={order} />
          ))
        ) : (
          <p>No orders found.</p>
        )}
      </div>
    </>
  );
}

export default OrdersPage;
