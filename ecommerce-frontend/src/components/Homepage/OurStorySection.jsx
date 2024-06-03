import React from 'react';
import './css/OurStorySection.css'; // Ensure you have this CSS file
import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function OurStorySection() {



    useEffect(() => {

        const params = new URLSearchParams(location.search);
        const scrollToStory = params.get('scrollToStory');

      
        if (scrollToStory === 'true') {
        const storySection = document.getElementById('story');
        if (storySection) {
            storySection.scrollIntoView({ behavior: 'smooth' });
        }
        }
    }, [location.search]);

    return (
        <div id='story' className="story-section">
            <div className="story-content">
                <h2>OUR STORY</h2>
                <h3>Discover the ecom experience</h3>
                <p>At Ecom App, we're passionate about providing the best shopping experience for our customers. Located in Stuttgart, our team is dedicated to curating a diverse range of high-quality products for every need and occasion. From fashion to electronics, we've got it all. Join us on this journey and discover the Ecom experience today!</p>

            </div>
            <div className="story-image">
                <img src="/images/xbox-story.png" alt="Our Story" />
            </div>
        </div>
    );
}

export default OurStorySection;
