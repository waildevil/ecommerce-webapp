import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import Navigbar from '../Homepage/Navigbar';
import './UpdateUserComponent.css'
import AuthService from '../../services/AuthService';


function UpdateUserComponent() {
    const navigate = useNavigate();
    const [userDetails, setUserDetails] = useState({
        email: '',
        username: '',
        name: '',
    });
    const [loading, setLoading] = useState(true);
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        const user = AuthService.getCurrentUser();
        if (user) {
            setUserId(user.userId);
        } else {
            console.error('No logged-in user found');
            navigate('/login');
        }
    }, [navigate]);

    useEffect(() => {
        if (userId) {
            const fetchUserData = async () => {
                try {
                    const response = await axios.get(`http://localhost:8080/api/users/${userId}`, {
                        headers: {
                            Authorization: `Bearer ${AuthService.getCurrentUser().token}`
                        }
                    });
                    setUserDetails(response.data);
                    setLoading(false);
                } catch (error) {
                    console.error('Error fetching user data:', error);
                }
            };

            fetchUserData();
        }
    }, [userId]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserDetails(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.put(`http://localhost:8080/api/users/updateUser/${userId}`, userDetails, {
                headers: {
                    Authorization: `Bearer ${AuthService.getCurrentUser().token}`
                }
            });
            alert('User updated successfully!');
            navigate('/profile');
        } catch (error) {
            console.error('Error updating user:', error);
            alert('Failed to update user.');
        }
    };

    if (loading) return <div>Loading...</div>;

    return (
        <>
            <Navigbar />
            <div className="update-user-container">
                <h2>Update User</h2>
                <form onSubmit={handleSubmit} className="update-user-form">
                    <label>
                        Email:
                        <input type="email" name="email" value={userDetails.email} onChange={handleInputChange} required />
                    </label>
                    <label>
                        Username:
                        <input type="text" name="username" value={userDetails.username} onChange={handleInputChange} required />
                    </label>
                    <label>
                        Name:
                        <input type="text" name="name" value={userDetails.name} onChange={handleInputChange} required />
                    </label>
                    <button type="submit" className="update-user-button">Update</button>
                </form>
            </div>
        </>
    );
}

export default UpdateUserComponent;
