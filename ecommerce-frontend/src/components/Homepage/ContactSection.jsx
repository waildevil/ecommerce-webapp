import React from 'react';
import './css/ContactSection.css'; 
import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function ContactSection() {

    const location = useLocation();

    useEffect(() => {

        const params = new URLSearchParams(location.search);
        const scrollToContact = params.get('scrollToContact');

        if (scrollToContact === 'true') {
        const contactSection = document.getElementById('contact');
        if (contactSection) {
            contactSection.scrollIntoView({ behavior: 'smooth' });
        }
        }
    }, [location.search]);
      
    return (
        <div id="contact" className="contact-section">
            <div className="contact-form">
                <h2>GET IN TOUCH</h2>
                <p>Feel free to give us a call or send us an email with your questions or comments.</p>
                <form>
                    <input type="text" placeholder="Name *" required />
                    <input type="email" placeholder="Email address *" required />
                    <input type="tel" placeholder="Phone number *" required />
                    <textarea placeholder="Message"></textarea>
                    <div className="checkbox-container">
                        <input type="checkbox" id="consent-checkbox" />
                        <label htmlFor="consent-checkbox">I allow this website to store my submission so they can respond to my inquiry.</label>
                    </div>
                    <button type="submit">Submit</button>
                </form>
            </div>
            <div className="contact-info">
                <h3>Get in touch</h3>
                <p><strong>Phone:</strong> +491771685549</p>
                <p><strong>Email:</strong> email@example.com</p>
                <h3>Location</h3>
                <p>Aachen, NRW DE</p>
                <h3>Hours</h3>
                <p>Monday - Friday: 9:00am - 10:00pm</p>
                <p>Saturday: 9:00am - 6:00pm</p>
                <p>Sunday: 9:00am - 11:00am</p>
            </div>
        </div>
    );
}

export default ContactSection;