import React,{useEffect, useState} from 'react'
import { deleteProduct, listProducts } from '../services/ProductService';
import { useNavigate } from 'react-router-dom';
import './Homepage/Navigbar.jsx'
import Navigbar from './Homepage/Navigbar.jsx';
import ProductCard from './Product/ProductCard.jsx';
import './ListProducts.css';

function ListProducts(){


    const [products, setProducts] = useState([]);
    const navigator = useNavigate();

    useEffect(() => {
        getAllProducts();
    },[]);

    function getAllProducts(){

        listProducts().then((response) => {
            console.log(response.data);
            setProducts(response.data);
        }).catch(error => {console.error(error);
        })
    }

    return(
        <>
      
            <Navigbar></Navigbar>
            <div className='list-products-container'>
                <h2 className='text-center' style={{paddingTop: "20px", marginBottom: "40px"}}>List of Products</h2>
                <div className="product-grid">
                    {products.map(product => (
                        <div key={product.id} className="product-grid-item">
                            <ProductCard 
                                product={product} 
                          
                            />
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}

export default ListProducts