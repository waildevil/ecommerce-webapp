import React from 'react';
import './css/Homepage.css';
import ContactSection from './ContactSection';
import Footer from './Footer';
import OurStorySection from './OurStorySection';
import FirstView from './FirstView';

function Homepage() {
    return (
      <>
        <FirstView/>
        <OurStorySection/>
        <ContactSection/>
        <Footer/>
      </>
    );
}

export default Homepage;