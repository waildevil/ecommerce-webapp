import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { deleteUser, getRoles, listUsers, searchUsers } from '../../services/UserService';
import Navigbar from '../Homepage/Navigbar';

function AdminListUsers() {
    const [users, setUsers] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [role, setRole] = useState("");
    const [roles, setRoles] = useState([]);
    const navigator = useNavigate();

    useEffect(() => {
        fetchRoles();
        if (searchTerm || role) {
            searchForUsers();
        } else {
            getAllUsers();
        }
    }, [searchTerm, role]);

    function fetchRoles() {
        getRoles().then((response) => {
            setRoles(response.data);
        }).catch(error => {
            console.error("Failed to fetch roles", error);
        });
    }

    function getAllUsers() {
        listUsers().then((response) => {
            setUsers(response.data);
        }).catch(error => {
            console.error(error);
        });
    }

    function addUser() {
        navigator('/admin/users/createUser');
    }

    function updateUser(userId) {
        navigator(`/admin/users/updateUser/${userId}`);
    }

    function viewProfile(userId) {
        navigator(`/admin/users/${userId}/profile`);
    }

    function removeUser(id) {
        deleteUser(id).then((response) => {
            getAllUsers();
        }).catch(error => {
            console.error(error);
        });
    }

    function searchForUsers() {
        searchUsers(searchTerm, role).then((response) => {
            setUsers(response.data);
        }).catch(error => {
            console.error(error);
        });
    }

    function handleSearch(event) {
        setSearchTerm(event.target.value);
    }

    function handleRoleChange(event) {
        setRole(event.target.value);
    }

    return (
        <>
            <Navigbar />
            <div className='container'>
                <h2 className='text-center' style={{ paddingTop: "20px" }}>List of Users</h2>
                <div className="row">
                    <div className="col-md-6">
                        <input
                            type="text"
                            className="form-control mb-2"
                            placeholder="Search by name or username"
                            value={searchTerm}
                            onChange={handleSearch}
                        />
                    </div>
                    <div className="col-md-6">
                        <select className="form-control mb-2" value={role} onChange={handleRoleChange}>
                            <option value="">All Roles</option>
                            {roles.map(role => (
                                <option key={role} value={role}>{role}</option>
                            ))}
                        </select>
                    </div>
                    <div className="col-md-6 text-right">
                        <button className='btn btn-primary mb-2' onClick={addUser}>Add User</button>
                    </div>
                </div>

                <table className='table table-striped table-bordered'>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td>{user.name}</td>
                                <td>{user.username}</td>
                                <td>{user.email}</td>
                                <td>{user.role}</td>
                                <td>{user.active ? 'Active' : 'Inactive'}</td>
                                <td>
                                    <button className='btn btn-info' onClick={() => updateUser(user.id)}>Update</button>
                                    <button className='btn btn-danger' onClick={() => removeUser(user.id)} style={{ marginLeft: '10px' }}>Deactivate</button>
                                    <button className='btn btn-primary' onClick={() => viewProfile(user.id)} style={{ marginLeft: '10px' }}>View Profile</button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </>
    );
}

export default AdminListUsers;
