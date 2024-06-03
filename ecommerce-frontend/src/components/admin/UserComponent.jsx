import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { createUser, getRoles, getUser, updateUser } from '../../services/UserService';
import './UserComponent.css'
import Navigbar from '../Homepage/Navigbar';

function UserComponent() {
    const [name, setName] = useState('');
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [isActive, setIsActive] = useState(true);
    const [roles, setRoles] = useState([]);
    const [selectedRole, setSelectedRole] = useState('');
    const [errors, setErrors] = useState({
        username: '',
        name: '',
        email: '',
        role: ''
    });
    const { id } = useParams();
    const navigator = useNavigate();

    useEffect(() => {
        getRoles().then(response => {
            setRoles(response.data);
        }).catch(error => {
            console.error('Failed to fetch roles', error);
        });
    }, []);

    useEffect(() => {
        if (id) {
            getUser(id).then(response => {
                const { name, username, email, active, role } = response.data;
                setName(name);
                setUsername(username);
                setEmail(email);
                setIsActive(active);
                setSelectedRole(role);
            }).catch(error => {
                console.error("Failed to fetch user details:", error);
            });
        }
    }, [id]);

    function saveOrUpdateUser(e) {
        e.preventDefault();

        if (validateForm()) {
            const user = { name, username, email, active: isActive, role: selectedRole };

            if (id) {
                updateUser(id, user).then(() => {
                    navigator('/admin/users');
                }).catch(error => {
                    console.error("Error updating user:", error);
                });
            } else {
                createUser(user).then(() => {
                    navigator('/admin/users');
                }).catch(error => {
                    console.error("Error creating user:", error);
                });
            }
        }
    }

    function validateForm() {
        let valid = true;
        const errorsCopy = { ...errors }

        if (name.trim()) {
            errorsCopy.name = '';
        } else {
            errorsCopy.name = 'Name is required';
            valid = false;
        }

        if (username.trim()) {
            errorsCopy.username = '';
        } else {
            errorsCopy.username = 'Username is required';
            valid = false;
        }

        if (email.trim()) {
            errorsCopy.email = '';
        } else {
            errorsCopy.email = 'Email is required';
            valid = false;
        }

        if (selectedRole.trim()) {
            errorsCopy.role = '';
        } else {
            errorsCopy.role = 'Role is required';
            valid = false;
        }

        setErrors(errorsCopy);
        return valid;
    }

    return (
        <>
            <Navigbar />
            <div className="user-form-container">
                <div className='container'>
                    <h2 className='text-center'>{id ? 'Update User' : 'Add User'}</h2>
                    <form>
                        <div className='form-group'>
                            <label className='form-label'>Name:</label>
                            <input
                                type='text'
                                placeholder='Enter User name'
                                name='name'
                                value={name}
                                className={`form-control ${errors.name ? 'is-invalid' : ''}`}
                                onChange={(e) => setName(e.target.value)}
                            />
                            {errors.name && <div className='invalid-feedback'>{errors.name}</div>}
                        </div>

                        <div className='form-group'>
                            <label className='form-label'>Username:</label>
                            <input
                                type='text'
                                placeholder='Enter username'
                                name='username'
                                value={username}
                                className={`form-control ${errors.username ? 'is-invalid' : ''}`}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                            {errors.username && <div className='invalid-feedback'>{errors.username}</div>}
                        </div>

                        <div className='form-group'>
                            <label className='form-label'>Email:</label>
                            <input
                                type='text'
                                placeholder='Enter email'
                                name='email'
                                value={email}
                                className={`form-control ${errors.email ? 'is-invalid' : ''}`}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                            {errors.email && <div className='invalid-feedback'>{errors.email}</div>}
                        </div>

                        <div className='form-group'>
                            <label className='form-label'>Role:</label>
                            <select
                                className={`form-control ${errors.role ? 'is-invalid' : ''}`}
                                value={selectedRole}
                                onChange={(e) => setSelectedRole(e.target.value)}
                            >
                                <option value="">Select a role</option>
                                {roles.map(role => (
                                    <option key={role} value={role}>{role}</option>
                                ))}
                            </select>
                            {errors.role && <div className='invalid-feedback'>{errors.role}</div>}
                        </div>

                        <div className='form-group'>
                            <label className='form-label'>Active:</label>
                            <label className="switch">
                                <input
                                    type="checkbox"
                                    checked={isActive}
                                    onChange={(e) => setIsActive(e.target.checked)}
                                />
                                <span className="slider"></span>
                            </label>
                        </div>

                        <button className='btn btn-success' onClick={saveOrUpdateUser}>Submit</button>
                    </form>
                </div>
            </div>
        </>
    );
}

export default UserComponent;
