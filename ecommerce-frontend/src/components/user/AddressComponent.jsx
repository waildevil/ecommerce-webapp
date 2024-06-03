import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './AddressComponent.css'; // Ensure you have this CSS file
import Navigbar from '../Homepage/Navigbar';
import { Link, useNavigate } from 'react-router-dom';
import AddressList from './AddressList';
import AuthService from '../../services/AuthService';

function AddressComponent() {

  const navigate = useNavigate();
  const [user, setUser] = useState({
    email: '',
    username: '',
    name:'',
    password:'',
    addresses: [],
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const currentUser = AuthService.getCurrentUser();
        console.log('Current user:', currentUser);
        if (!currentUser || !currentUser.userId) {
          console.error('No logged-in user found');
          setLoading(false);
          return;
        }

        const response = await axios.get(`http://localhost:8080/api/users/${currentUser.userId}`);
        setUser(response.data);
      } catch (error) {
        console.error('Error fetching user data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchUserData();
  }, []);


  const handleEditPassword = () => {
    navigate('/change-password');
  };

  const handleEditProfile = () => {
    navigate('/update-user');
  };

  if (loading) return <div>Loading user profile...</div>;

  

            return (
                <>
                <Navigbar></Navigbar>

                        <h1 style={{textAlign:'center', paddingTop: '50px', paddingBottom:'40px'}}>User Profile</h1>
                        <div style={{textAlign:'center'}} className="user-details">
                            <p><strong>Email:</strong> {user.email}</p>
                            <p><strong>Username:</strong> {user.username}</p>
                            <p><strong>Name:</strong> {user.name}</p>
                            <button className="edit-button" onClick={handleEditProfile}>Edit Profile</button>
                            <button className="edit-button" onClick={handleEditPassword}>Edit Password</button>
                            
                            
                        </div>

                        <h1 style={{textAlign:'center', paddingTop: '50px', paddingBottom:'40px'}}>User Addresses</h1>
                        <AddressList user={user} setUser={setUser}/>
             
                </>
              );
}

export default AddressComponent;
