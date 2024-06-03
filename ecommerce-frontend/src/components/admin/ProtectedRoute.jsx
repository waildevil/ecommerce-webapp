import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import AuthService from '../../services/AuthService';

const ProtectedRoute = ({ roles }) => {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser) {
       
        return <Navigate to="/login" />;
    }

    if (roles && roles.indexOf(currentUser.role) === -1) {
  
        return <Navigate to="/forbidden" />;
    }


    return <Outlet />;
};

export default ProtectedRoute;
