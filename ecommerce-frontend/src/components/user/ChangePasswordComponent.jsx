import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './ChangePasswordComponent.css';
import Navigbar from '../Homepage/Navigbar';
import AuthService from '../../services/AuthService';

function ChangePasswordComponent() {
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

    const user = AuthService.getCurrentUser();
    let userId;

    if (user && user.userId) {
        userId = user.userId;
    } else {
        console.error('User not found');
        // Handle the case where the user is not available
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (newPassword.length < 8) {
            setError("The new password must be at least 8 characters long.");
            return;
        }

        if (newPassword !== confirmPassword) {
            setError("New passwords don't match.");
            return;
        }

        try {
            const response = await axios.put(`http://localhost:8080/api/users/${userId}/change-password`, {
                currentPassword,
                newPassword
            });

            if (response.status === 200) {
                alert('Password changed successfully!');
                navigate('/profile');
            } else {
                setError('Error changing password.');
            }
        } catch (err) {
            console.error('Failed to change password:', err);
            setError('Failed to change password. Make sure you entered the correct current password.');
        }
    };

    return (
        <>
            <Navigbar />
            <div className="change-password-container" style={{ marginTop: '50px' }}>
                <h2>Change Password</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        Current Password:
                        <input
                            type="password"
                            name='currentPassword'
                            value={currentPassword}
                            onChange={(e) => setCurrentPassword(e.target.value)}
                            required
                        />
                    </label>
                    <br />
                    <label>
                        New Password:
                        <input
                            type="password"
                            name='newPassword'
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            required
                        />
                    </label>
                    <br />
                    <label>
                        Confirm New Password:
                        <input
                            type="password"
                            name='confirmPassword'
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </label>
                    <br />
                    <button type="submit">Change Password</button>
                </form>
                {error && <p className="error-message">{error}</p>}
                {success && <p className="success-message">{success}</p>}
            </div>
        </>
    );
}

export default ChangePasswordComponent;
