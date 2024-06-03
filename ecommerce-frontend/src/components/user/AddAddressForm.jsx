import React, { useState , useMemo } from 'react';
import axios from 'axios';
import { useLocation, useNavigate } from 'react-router-dom';
import CountrySelector from './CountrySelector';
import './AddAddressForm.css'
import Navigbar from '../Homepage/Navigbar';
import AuthService from '../../services/AuthService';


function AddAddressForm() {

    const navigate = useNavigate();
    const location = useLocation();
    const redirectPath = location.state?.redirectPath || '/profile';


    const user = AuthService.getCurrentUser();
    const userId = user.userId;
    const [address, setAddress] = useState({
        name:'',
        addressLine1: '',
        addressLine2: '',
        city: '',
        state: '',
        zipCode: '',
        country: '',
        phoneNumber:''
    });

    

    const handleCountryChange = (selectedOption) => {
        setAddress(prevState => ({ ...prevState, country: selectedOption.label }));
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setAddress({ ...address, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const addressData = { ...address, userId: userId };
            const response = await axios.post('http://localhost:8080/api/addresses/createAddress', addressData);
            alert('Address added successfully!');
            navigate(redirectPath);
        } catch (error) {
            console.error('Error adding the address:', error);
            alert('Failed to add the address.');
        }
    };

  return (
    <>
        <Navigbar></Navigbar>
        <form onSubmit={handleSubmit} className="add-address-form">
        <h2>Add New Address</h2>
        <label>
            Name:
            <input
            type="text"
            name="name"
            value={address.name}
            onChange={handleInputChange}
            required
            />
        </label>

        <label>
            Address Line 1:
            <input
            type="text"
            name="addressLine1"
            value={address.addressLine1}
            onChange={handleInputChange}
            required
            />
        </label>

        <label>
            Address Line 2:
            <input
            type="text"
            name="addressLine2"
            value={address.addressLine2}
            onChange={handleInputChange}
            />
        </label>

        <label>
            Phone Number:
            <input
            type="text"
            name="phoneNumber"
            value={address.phoneNumber}
            onChange={handleInputChange}
            required
            />
        </label>
        
        <label>
            State/Province/Region:
            <input
            type="text"
            name="state"
            value={address.state}
            onChange={handleInputChange}
            />
        </label>

        <label>
            City:
            <input
            type="text"
            name="city"
            value={address.city}
            onChange={handleInputChange}
            required
            />
        </label>

        <label>
            ZIP/Postal Code:
            <input  
            type="text"
            name="zipCode"
            value={address.zipCode}
            onChange={handleInputChange}
            required
            />
        </label>

        <label>
            Country:
            <CountrySelector value={address.country} onChange={handleCountryChange} />
        </label>
        <button type="submit">Add Address</button>
        </form>
    </>
  );
}

export default AddAddressForm;
