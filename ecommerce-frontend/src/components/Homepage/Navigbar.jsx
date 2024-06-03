import React, { useState, useEffect, useRef  } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import './css/Navigbar.css';
import axios from "axios";
import AuthService from '../../services/AuthService'

function Navigbar (){


  const [searchQuery, setSearchQuery] = useState('');
  const [categories, setCategories] = useState([]);
  const [selectedCategoryId, setSelectedCategoryId] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState('');
  const [username, setUsername] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const navigate = useNavigate();


  useEffect(() => {
    const user = AuthService.getCurrentUser();
    if (user) {
      setIsLoggedIn(true);
      setUserRole(user.role);
      axios.get(`http://localhost:8080/api/users/${user.userId}`, {
        headers: {
          Authorization: `Bearer ${user.token}`
        }
      })
      .then(response => {
        setUsername(response.data.username);
      })
      .catch(error => {
        console.error('Failed to fetch user data', error);
      });
  }
}, []);


const handleLogout = () => {
  AuthService.logout();
  setIsLoggedIn(false);
  navigate('/');
  window.location.reload();
};


  const handleSearch = (event) => {
    event.preventDefault();
    const categoryPart = selectedCategoryId && selectedCategoryId !== 'All' ? `&categoryId=${selectedCategoryId}` : '';
    const searchUrl = `/search?searchTerm=${encodeURIComponent(searchQuery)}${categoryPart}`;
    navigate(searchUrl, { state: { selectedCategoryId } });
};
  

return (
  <div className="homepage">
    <nav className="top-nav" style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
      <div style={{ display: 'flex', alignItems: 'center' }}>
        <Link to="/" className="brand" style={{ marginRight: '20px' }}>
          <img className="logo-image" src="/images/logo.png" alt="Blog Logo" />
        </Link>
        {isLoggedIn && <span className="username-display" style={{ marginRight: '20px' }}>Welcome, {username}!</span>}
      </div>

      {isLoggedIn && userRole === 'USER' && (
        <form className="search-bar" onSubmit={handleSearch} style={{ flex: 1 }}>
          <input
            className="search-input"
            type="search"
            placeholder="Search products..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            style={{ width: '100%' }}
          />
          <button className="search-button" type="submit">🔍</button>
        </form>
      )}

      <div className="nav-links" style={{ display: 'flex', alignItems: 'center' }}>
        <Link to='/products' style={{ marginRight: '20px' }}>Products</Link>
        {isLoggedIn ? (
          <>
            {userRole === 'USER' && <Link to='/orders' style={{ marginRight: '20px' }}>Orders</Link>}
            {userRole === 'ADMIN' && <Link to='/admin/users' style={{ marginRight: '20px' }}>Users List</Link>}
            {userRole === 'ADMIN' && <Link to='/admin/products' style={{ marginRight: '20px' }}>Products List</Link>}
            <Link to='/profile' style={{ marginRight: '20px' }}>Profile</Link>
            <Link className="logout-button" onClick={handleLogout} style={{ marginRight: '20px' }}>Logout</Link>
            {userRole === 'USER' && (
              <div className="nav-cart">
                <Link to="/cart">🛒</Link>
              </div>
            )}
          </>
        ) : (
          <>
          <Link to='/login'>Login</Link>
          <Link to='/register'>Register</Link>
          </>
        )}
      </div>
    </nav>
  </div>
);
};

export default Navigbar;
