import React from 'react';
import Navigbar from '../Homepage/Navigbar';
import './Forbidden.css'

const Forbidden = () => {
    return (
        <>
            <Navigbar />
            <div className="forbidden-container">
                <h1 className="forbidden-title">403 - Forbidden</h1>
                <p className="forbidden-message">You do not have permission to view this page.</p>
            </div>
        </>
    );
};

export default Forbidden;
