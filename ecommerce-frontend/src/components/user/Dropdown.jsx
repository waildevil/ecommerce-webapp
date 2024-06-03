import React, { useState, useRef, useEffect } from 'react';
import './Dropdown.css'; // Ensure you link to your CSS file correctly

function Dropdown() {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [dropdownRef]);

  return (
    <div className="dropdown-container" ref={dropdownRef}>
      <button className="dropdown-button" onClick={() => setIsOpen(!isOpen)}>
        Account & Lists
      </button>
      {isOpen && (
        <div className="dropdown-content">
          <a href="#">Your Account</a>
          <a href="#">Your Orders</a>
          {/* Add more dropdown items here */}
          <a href="#">Sign Out</a>
        </div>
      )}
    </div>
  );
}

export default Dropdown;
