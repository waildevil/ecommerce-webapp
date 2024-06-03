import React, { useEffect, useState } from 'react'
import { createProduct, getCategories, getProduct, updateProduct } from '../../services/ProductService';
import { useNavigate, useParams } from 'react-router-dom';
import './ProductComponent.css'
import Navigbar from '../Homepage/Navigbar';
import { updateUser } from '../../services/UserService';


function ProductComponent (){

    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState();
    const [quantity, setQuantity] = useState();
    const [categories, setCategories] = useState([]);
    const [selectedCategoryId, setSelectedCategoryId] = useState('');
    const [isActive, setIsActive] = useState(true);
    const [errors, setErrors] = useState({
        name:'',
        description:'',
        price:'',
        quantity:''
    });

    const navigator = useNavigate();

    const {id} = useParams();

    useEffect(() => {
        fetchCategories();
    }, []);

    useEffect(() => {
        getCategories().then(response => {
            const fetchedCategories = response.data;
            setCategories(fetchedCategories);
    

            if (id) {
                getProduct(id).then(response => {
                    const { name, description, price, quantity, category, active } = response.data;
                    setName(name);
                    setDescription(description);
                    setPrice(price);
                    setQuantity(quantity);
                    setIsActive(active);
                    if (category) {
                        setSelectedCategoryId(category.id);
                    } else if (fetchedCategories.length > 0) {
                        setSelectedCategoryId(fetchedCategories[0].id);
                    }
                }).catch(error => {
                    console.error("Failed to fetch product details:", error);
                });
            }
        }).catch(error => {
            console.error("Failed to fetch categories:", error);
        });
    }, [id]);
    
    

    function fetchCategories() {
        getCategories().then(response => {
            setCategories(response.data);
            if (response.data.length > 0 && !selectedCategoryId) {
                setSelectedCategoryId(response.data[0].id);
            }
        }).catch(error => {
            console.error('Failed to fetch categories', error);
        });
    }

    function saveOrUpdateProduct(e){
        e.preventDefault();

        if(validateForm()){

            const product = {name,description,price,quantity, category: { id: selectedCategoryId }, active:isActive};
            console.log("Submitting product: ",product);
            
            if(id){
                updateProduct(id, product).then((response) => {
                    console.log("Update response: ",response.data);
                    navigator('/admin/products')
                }).catch(error =>{
                    console.error(error);
                })
            } else {
                createProduct(product).then((response) => {
                    console.log("Create response: ", response.data);
                    navigator('/admin/products')
                }).catch(error => {
                    console.error(error);
                })
            }
             
        }
 

    }

    function validateForm(){
        let valid = true;
        const errorsCopy = {...errors}

        if(name.trim()){
            errorsCopy.name = '';
        }else{
            errorsCopy.name = 'Name is required';
            valid = false;
        }

        if(description.trim()){
            errorsCopy.description = '';
        }else{
            errorsCopy.description = 'Description is required';
            valid = false;
        }

        if (!price) {
            errorsCopy.price = 'Price is required';
            valid = false;
           } 
        else if (isNaN(price)) {
            errorsCopy.price = 'Price must be a number';
            valid = false;
        }else{
            errorsCopy.price='';
        }

        if (!quantity) {
            errorsCopy.quantity = 'Quantity is required';
            valid = false;
           } 
        else if (isNaN(quantity)) {
            errorsCopy.quantity = 'Quantity must be a number';
            valid = false;
        }else{
            errorsCopy.quantity='';
        }


        setErrors(errorsCopy);
        return valid;
    }



  return (
    <>
        <Navigbar></Navigbar>
        <div className="product-form-container">
            <div className='container'>
                <h2 className='text-center'>{id ? 'Update Product' : 'Add Product'}</h2>
                <form>
                    <div className='form-group'>
                        <label className='form-label'>Name:</label>
                        <input
                            type='text'
                            placeholder='Enter Product name'
                            name='name'
                            value={name}
                            className={`form-control ${errors.name ? 'is-invalid' : ''}`}
                            onChange={(e) => setName(e.target.value)}
                        />
                        {errors.name && <div className='invalid-feedback'>{errors.name}</div>}
                    </div>

                    <div className='form-group'>
                        <label className='form-label'>Description:</label>
                        <input
                            type='text'
                            placeholder='Enter Product description'
                            name='description'
                            value={description}
                            className={`form-control ${errors.description ? 'is-invalid' : ''}`}
                            onChange={(e) => setDescription(e.target.value)}
                        />
                        {errors.description && <div className='invalid-feedback'>{errors.description}</div>}
                    </div>

                    <div className='form-group'>
                        <label className='form-label'>Price:</label>
                        <input
                            type='text'
                            placeholder='Enter Product Price'
                            name='price'
                            value={price}
                            className={`form-control ${errors.price ? 'is-invalid' : ''}`}
                            onChange={(e) => setPrice(e.target.value)}
                        />
                        {errors.price && <div className='invalid-feedback'>{errors.price}</div>}
                    </div>

                    <div className='form-group'>
                        <label className='form-label'>Quantity:</label>
                        <input
                            type='text'
                            placeholder='Enter Product quantity'
                            name='quantity'
                            value={quantity}
                            className={`form-control ${errors.quantity ? 'is-invalid' : ''}`}
                            onChange={(e) => setQuantity(e.target.value)}
                        />
                        {errors.quantity && <div className='invalid-feedback'>{errors.quantity}</div>}
                    </div>

                    <div className='form-group'>
                            <label className='form-label'>Category:</label>
                            <select
                                className='form-control'
                                value={selectedCategoryId}
                                onChange={(e) => setSelectedCategoryId(e.target.value)}
                            >
                                {categories.map(category => (
                                    <option key={category.id} value={category.id}>{category.name}</option>
                                ))}
                            </select>
                    </div>

                    <div className='form-group'>
                        <label className='form-label'>Active:</label>
                        <label className="switch">
                            <input
                                type='checkbox'
                                checked={isActive}
                                onChange={(e) => {setIsActive(e.target.checked); console.log("isActive:", e.target.checked);}}
                                
                            />
                            <span className="slider"></span>
                            </label>
                        
                    </div>                

                    <button className='btn btn-success' onClick={saveOrUpdateProduct}>Submit</button>
                </form>
            </div>
        </div>
    </>
  )
};

export default ProductComponent;

