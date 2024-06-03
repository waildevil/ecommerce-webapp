import React from 'react'
import './css/Homepage.css'
import Navigbar from './Navigbar.jsx';
import {Link} from 'react-router-dom'

function FirstView (){
  return (
    <div className="homepage">
            <Navigbar/>
            <header className="hero"> 
                <h1 className="hero-title">Discover Ecom</h1>
                <p className="hero-subtitle">Explore our amazing collection of products</p>
                <a href="/products" className="hero-button">View products</a>

            </header>

            
      </div>
  )
};

export default FirstView;
