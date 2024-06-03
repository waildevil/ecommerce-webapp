// AddressList.js
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './AddressComponent.css';
import AuthService from '../../services/AuthService';

function AddressList({ user, setUser, redirectPath }) {
  
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState('');
  const navigate = useNavigate();


  useEffect(() => {
    const user = AuthService.getCurrentUser();
    if (user) {
      setIsLoggedIn(true);
      setUserRole(user.role);
  }
}, []);


  const handleAddAddress = () => {
    navigate('/add-address', { state: { redirectPath } })
  };

  const handleEditAddress = (addressId) => {
    navigate(`/update-address/${addressId}`, { state: { redirectPath } });
  };

  const handleRemoveAddress = async (addressId) => {
    try {
      await axios.delete(`http://localhost:8080/api/addresses/deleteAddress/${addressId}`);
      alert('Address removed successfully!');
      const updatedAddresses = user.addresses.filter(address => address.id !== addressId);
      setUser({ ...user, addresses: updatedAddresses });
    } catch (error) {
      console.error('Error removing the address:', error);
      alert('Failed to remove the address.');
    }
  };

  return (
    <div className="addresses-container">
      

      {isLoggedIn ? (
        <>
          {userRole === 'USER' && 
            <Link to="/add-address" className="add-address-link">
              <div className="address-card add-address-card" onClick={handleAddAddress}>
                <span className="plus-icon">+</span>
                <span className="add-address-text">Add Address</span>
              </div>
            </Link>}

          
                  {user.addresses.map((address, index) => (
                    <div key={index} className="address-card">
                      <div className="address-card-header">
                        {address.isDefault ? "Default:" : ""} {address.type}
                      </div>
                      <div className="address-details">
                        <div><strong>{address.name}</strong></div>
                        <div>{address.addressLine1}</div>
                        <div>{address.addressLine2}</div>
                        <div>{address.city}, {address.state} {address.zipCode}</div>
                        <div>{address.country}</div>
                        <div>Phone: {address.phoneNumber}</div>
                      </div>
                      <div className="address-actions">
                        <button onClick={() => handleEditAddress(address.id)} className="edit-button">Edit</button>
                        <button onClick={() => handleRemoveAddress(address.id)} className="remove-button">Remove</button>
                      </div>
                    </div>
               ))}
                            
                        </>
                    ) : (
                        <Link to='/login'>Login</Link>
                    )}
      

      
    </div>
  );
}

export default AddressList;
