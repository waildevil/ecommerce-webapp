import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import CountrySelector from './CountrySelector'; // Assuming you have a CountrySelector component
import './AddAddressForm.css'
import Navigbar from '../Homepage/Navigbar';

function UpdateAddressForm() {
  const { addressId } = useParams();
  const navigate = useNavigate();
  const [address, setAddress] = useState({
    name: '',
    addressLine1: '',
    addressLine2: '',
    city: '',
    state: '',
    zipCode: '',
    country: '',
    phoneNumber: ''
  });

  useEffect(() => {
    axios.get(`http://localhost:8080/api/addresses/${addressId}`)
      .then(response => setAddress(response.data))
      .catch(error => console.error('Error fetching address:', error));
  }, [addressId]);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setAddress({ ...address, [name]: value });
  };

  const handleCountryChange = (selectedOption) => {
    setAddress({ ...address, country: selectedOption.value });
  };
  
  const handleSubmit = (event) => {
    event.preventDefault();
    axios.put(`http://localhost:8080/api/addresses/updateAddress/${addressId}`, address)
      .then(() => {
        alert('Address updated successfully!');
        navigate('/profile'); // Assuming you want to redirect to the profile page after the update
      })
      .catch(error => {
        console.error('Error updating address:', error);
        alert('Failed to update the address.');
      });
  };

  return (
    <>
        <Navigbar></Navigbar>
        <form onSubmit={handleSubmit} className="add-address-form">
        <h2>Edit Address</h2>
        <label>
            Name:
            <input type="text" name="name" value={address.name} onChange={handleInputChange} required />
        </label>
        <label>
            Address Line 1:
            <input type="text" name="addressLine1" value={address.addressLine1} onChange={handleInputChange} required />
        </label>
        <label>
            Address Line 2:
            <input type="text" name="addressLine2" value={address.addressLine2} onChange={handleInputChange} />
        </label>
        <label>
            City:
            <input type="text" name="city" value={address.city} onChange={handleInputChange} required />
        </label>
        <label>
            State/Province/Region:
            <input type="text" name="state" value={address.state} onChange={handleInputChange} />
        </label>
        <label>
            ZIP/Postal Code:
            <input type="text" name="zipCode" value={address.zipCode} onChange={handleInputChange} required />
        </label>
        <label>
            Country:
            <CountrySelector value={address.country} onChange={handleCountryChange} />
        </label>
        <label>
            Phone Number:
            <input type="text" name="phoneNumber" value={address.phoneNumber} onChange={handleInputChange} required />
        </label>
        <button type="submit">Update Address</button>
        </form>
    </>
  );
}

export default UpdateAddressForm;
