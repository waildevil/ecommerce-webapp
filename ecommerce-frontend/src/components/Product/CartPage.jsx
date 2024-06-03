import React, { useEffect, useState } from 'react';
import './CartPage.css'
import { Link,useNavigate  } from 'react-router-dom';
import Navigbar from '../Homepage/Navigbar';
import axios from "axios";
import AuthService from '../../services/AuthService';

const CartPage = () => {
  const [cart, setCart] = useState({ items: [], totalPrice: 0 });
  const [isLoading, setIsLoading] = useState(true);
  const [quantities, setQuantities] = useState({});
  const [error, setError] = useState(null);

  const [loading, setLoading] = useState(true);
  const [totalQuantity, setTotalQuantity] = useState(0);

  const user = AuthService.getCurrentUser();
  const userId = user.userId;
  

useEffect(() => {

  
    console.log('Retrieved user from local storage:', user);
    if (!user) {
      setError('No logged-in user found');
      setLoading(false);
      return;
    }
    const fetchCartData = async () => {
        setIsLoading(true);
        try {
            console.log(`Fetching cart for user ${userId}`);
            const response = await fetch(`http://localhost:8080/api/cart/${userId}`);
            console.log('Response:', response);

            if (!response.ok) {
                throw new Error(`Error: ${response.statusText}`);
            }

            const cartData = await response.json();
            console.log('Cart data:', cartData);
            setCart(cartData);


            const newTotalQuantity = cartData.items.reduce((total, item) => total + item.quantity, 0);
            setTotalQuantity(newTotalQuantity);
        } catch (err) {
            setError(err.message);
            console.error('Error fetching cart:', err);
        } finally {
            setIsLoading(false);
        }
    };

    if (userId) {
        fetchCartData();
    }
}, []);



  const handleQuantityChange = async (cartItemId, newQuantity) => {
    newQuantity = parseInt(newQuantity, 10);
    if (newQuantity > 0) {
      setQuantities((prevQuantities) => ({
        ...prevQuantities,
        [cartItemId]: newQuantity,
      }));
      await updateQuantityInCart(cartItemId, newQuantity);
    }
  };


    const updateQuantityInCart = async (cartItemId, newQuantity) => {
      try {
        newQuantity = parseInt(newQuantity, 10);
        if (newQuantity > 0) {
          const response = await axios.put(
            `http://localhost:8080/api/cart/items/update/${cartItemId}?quantity=${newQuantity}`
          );
          const updatedCartItem = response.data; 
    
          setCart((prevCart) => {
   
            const updatedItems = prevCart.items.map((item) => {
              if (item.id === cartItemId) {
                return { ...item, quantity: newQuantity, subtotal: item.product.price * newQuantity };
              }
              return item;
            });
    
          
            const newTotalQuantity = updatedItems.reduce((total, item) => total + item.quantity, 0);
    
            
            setTotalQuantity(newTotalQuantity);
    
            
            const updatedTotalPrice = updatedItems.reduce((total, item) => total + item.subtotal, 0);
    
            
            return { ...prevCart, items: updatedItems, totalPrice: updatedTotalPrice };
          });
    
          console.log('Cart item updated successfully:', updatedCartItem);
        }
      } catch (error) {
        console.error('Error updating item quantity in cart:', error);
      }
    };
    



  const handleRemoveItem = async (cartItemId) => {
    try {
      const response = await fetch(`http://localhost:8080/api/cart/items/remove/${cartItemId}`, {
        method: 'DELETE'
      });
  
      if (!response.ok) {
        throw new Error('Failed to remove item from cart');
      }
  
      
      setCart((prevCart) => ({
        ...prevCart,
        items: prevCart.items.filter((item) => item.id !== cartItemId),
        totalPrice: prevCart.items
          .filter((item) => item.id !== cartItemId)
          .reduce((total, item) => total + item.subtotal, 0)
      }));
  
    } catch (error) {
      console.error('Error removing item from cart:', error);

    }
  };

  const handleCheckout = async () => {
    try {
        const totalAmount = cart.totalPrice;

        const paymentResponse = await axios.post(
          `http://localhost:8080/api/paypal/pay`,
          cart.items,
          {
            params: {
              sum: totalAmount,
              jwt: user.token
            },
            headers: {
              'Authorization': `Bearer ${user.token}`,
              'Content-Type': 'application/json'
            }
          }
        );

        const approvalUrl = paymentResponse.data;
        console.log('Redirecting to PayPal approval URL:', approvalUrl);

        window.open(approvalUrl, '_blank');

        await clearCart(userId);
    } catch (error) {
        console.error('Error processing checkout:', error);
        alert('Failed to process checkout: ' + error.response?.data?.message || error.message);
    }
};

const clearCart = async (userId) => {
  try {
    await axios.post(`http://localhost:8080/api/cart/${userId}/clear`, null, {
      headers: {
        Authorization: `Bearer ${user.token}`,
      },
    });
    console.log('Cart cleared successfully');
    setCart({ items: [], totalPrice: 0 });
    setTotalQuantity(0);
  } catch (error) {
    console.error('Error clearing cart:', error);
  }
};


const createOrderFromCart = async () => {
    try {
        const response = await axios.post('http://localhost:8080/api/orders/createOrderFromCart', {}, {
            headers: {
                Authorization: `Bearer ${user.token}`
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error creating order from cart:', error);
        throw error;
    }
};



  


  if (isLoading) return <div>Loading cart...</div>;
  if (error) return <div>Error fetching cart: {error}</div>;
  if (!cart) return <div>Cart is empty or not found</div>;


  return (
    <>
    <Navigbar></Navigbar>
    <div className="cart-page">
      <h1 className="cart-header">Your Cart</h1>
      <div className="cart-content">
        <div className="cart-items">
          {cart.items.map((item) => (
            <div key={item.id}  className="cart-item">
              <Link to={`/products/${item.product.id}`} className="cart-item-link">
                <img src={`http://localhost:8080${item.product.imageUrl}`} alt={item.product.name} className="cart-item-img" />
              </Link>
              <div className="cart-item-info">
                <Link to={`/products/${item.product.id}`} className="cart-item-title">
                  {item.product.name}
                </Link>
                <div className="cart-item-price">{item.product.price}€</div>
                <input
                    type="number"
                    value={quantities[item.id] || item.quantity}
                    onChange={(e) => handleQuantityChange(item.id, e.target.value)}
                    min="1"
                 
                  />
                  <button onClick={() => handleRemoveItem(item.id)} className="remove-item-btn">
                    Remove
                  </button>
                <div className="cart-item-subtotal">Subtotal: {item.subtotal}€</div>
              </div>
            </div>
          ))}
        </div>
        <div className="cart-summary">
          <div className="cart-subtotal">
            <span>Subtotal ({totalQuantity} items): </span>
            <span>{cart.totalPrice}€</span>
          </div>
          <button onClick={handleCheckout} className="checkout-btn">Proceed to Checkout</button>
        </div>
      </div>
    </div>
    </>
  );
};

export default CartPage;
