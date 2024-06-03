import React from 'react';
import './css/Footer.css';

function Footer() {
    return (
        <footer className="footer">
            <div className="footer-content">
                <p>&copy; {(new Date().getFullYear())} Ecom App. All rights reserved.</p>
                <p>Privacy Policy | Terms of Use | Sitemap</p>
            </div>
        </footer>
    );
}

export default Footer;
