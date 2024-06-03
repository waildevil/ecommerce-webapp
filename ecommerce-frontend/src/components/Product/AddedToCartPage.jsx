import React from 'react';
import { Link, useLocation  } from 'react-router-dom';
import './AddedToCartPage.css';
import Navigbar from '../Homepage/Navigbar';

function AddedToCartPage (){

  const location = useLocation();
  const { item, subtotal, totalItems } = location.state || {};
  return (
    <>
    <Navigbar></Navigbar>
        <div className="added-to-basket-container">
            <div className="added-to-basket">
                <h2>✓ Added to Basket</h2> 
                <p>{item.name}</p>
                <img src={`http://localhost:8080${item.imageUrl}`} alt={item.name} />
            </div>
            <div className="basket-subtotal">
                <h3>Basket Subtotal:</h3>
                <p>€{subtotal}</p>

                <Link to="/cart" className="go-to-basket-btn">
                Go to basket
                </Link>
            </div>
        </div>
    </>
  )
};

export default AddedToCartPage;
