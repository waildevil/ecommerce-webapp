import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { searchProductsByName } from '../../services/ProductService.jsx';
import ProductCard from '../Product/ProductCard.jsx';
import './css/SearchResults.css'
import Navigbar from './Navigbar.jsx';
import axios from "axios";

function SearchResults() {
  const [products, setProducts] = useState([]);
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const name = searchParams.get('searchTerm');
  const { selectedCategoryId } = location.state || {};

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const categoryPart = selectedCategoryId && selectedCategoryId !== 'All' 
                            ? `&categoryId=${selectedCategoryId}` : '';
        const response = await axios.get(`http://localhost:8080/api/products/search?searchTerm=${encodeURIComponent(name)}${categoryPart}`);
        setProducts(response.data);
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    if (name) {
      fetchProducts();
    }
  }, [name, selectedCategoryId]);



  return (
    <>
      <Navigbar />
      <div className="search-results-page">
        <h1>Search Results</h1>
        <div className="product-grid">
          {products.length > 0 ? (
            products.map((product) => <ProductCard key={product.id} product={product} />)
          ) : (
            <p>No products found for "{name}".</p>
          )}
        </div>
      </div>
    </>
  );
}

export default SearchResults;
