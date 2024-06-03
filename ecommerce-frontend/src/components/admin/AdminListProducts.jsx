import React,{useEffect, useState} from 'react'
import { deleteProduct, getCategories, listProducts, searchProducts } from '../../services/ProductService.jsx';
import { Link, useNavigate } from 'react-router-dom';
import ProductCard from '../Product/ProductCard.jsx';
import Navigbar from '../Homepage/Navigbar.jsx';


function AdminListProducts(){


    const [products, setProducts] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [categoryId, setCategoryId] = useState(null);
    const [categories, setCategories] = useState([]);
    const navigator = useNavigate();

    useEffect(() => {
        fetchCategories();
        if (searchTerm || categoryId !== null) {
            searchForProducts();
        } else {
            getAllProducts();
        }
    }, [searchTerm, categoryId]);

    useEffect(() => {
        getAllProducts();
    },[]);

    function getAllProducts(){
        listProducts().then((response) => {
            setProducts(response.data);
        }).catch(error => {console.error(error);
        })
    }


    const fetchCategories = () => {
        getCategories().then((response) => {
            setCategories(response.data);
        }).catch((error) => {
            console.error("Failed to fetch roles", error);
        });
    }

    

    function addProduct() {
        navigator("/admin/products/createProduct");
    }

    function updateProduct(id){
        navigator(`/admin/products/updateProduct/${id}`);
    }

    function removeProduct(id){
        deleteProduct(id).then((response) => {
            getAllProducts();
        }).catch(error => {
            console.error(error);
        })
    }


    function searchForProducts() {
        searchProducts(searchTerm, categoryId).then((response) => {
            setProducts(response.data);
        }).catch(error => {
            console.error(error);
        });
    }

    function handleSearch(event) {
        setSearchTerm(event.target.value);
    }

    function handleCategoryChange(event) {
        setCategoryId(event.target.value ? parseInt(event.target.value) : null);
    }

    return(
        <>
      
        <Navigbar></Navigbar>
            <div className='container'>
                <h2 className='text-center' style={{ paddingTop: "20px" }}>List of Products</h2>


                <div className="row">
                    <div className="col-md-6">
                        <input 
                            type="text" 
                            className="form-control mb-2" 
                            placeholder="Search by name" 
                            value={searchTerm} 
                            onChange={handleSearch} 
                        />
                    </div>
                    <div className="col-md-6">
                        <select className="form-control mb-2" value={categoryId || ''} onChange={handleCategoryChange}>
                            <option value="">All Categories</option>
                            {categories.map(category => (
                                <option key={category.id} value={category.id}>{category.name}</option>
                            ))}
                        </select>
                    </div>
                    <div className="col-md-6 text-right">
                    <button className='btn btn-primary mb-2' onClick={addProduct}>Add Product</button>
                    </div>
                </div>

                


                <table className='table table-striped table-bordered'>
                    <thead>
                        <tr>
                            <th>Image</th>
                            <th>Product Name</th>
                            <th>Description</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Category</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {products.map(product => (
                            <tr key={product.id}>
                                <td>
                                    <img src={`http://localhost:8080${product.imageUrl}`} style={{ width: '55px', height: '50px' }} />
                                </td>
                                
                                <td>
                                    <Link style={{textDecoration:"none",color:"#007185"}} to={`/products/${product.id}`}
                                    >{product.name}</Link>
                                </td>
                                

                                <td>{product.description}</td>
                                <td>{product.price.toFixed(2)}</td>
                                <td>{product.quantity}</td>
                                <td>{product.category ? product.category.name : 'No Category'}</td>
                                <td>{product.active ? 'Active' : 'Inactive'}</td>
                                <td>
                                    <button className='btn btn-info' onClick={() => updateProduct(product.id)}>Update</button>
                                    <button className='btn btn-danger' onClick={() => removeProduct(product.id)}
                                        style={{ marginLeft: '10px' }}
                                    >Deactivate</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>

            </div>
        </>
    );
}

export default AdminListProducts