import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getUser } from '../../services/UserService';
import Navigbar from '../Homepage/Navigbar';
import OrdersPage from '../user/OrdersPage';
import AddressList from '../user/AddressList';
import OrderCard from '../user/OrderCard';
import './UserProfile.css'

function UserProfile() {
    const { userId } = useParams(); 
    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await getUser(userId);
                const userData = response.data;
                console.log('Fetched user data for orders:', userData);
                const sortedOrders = userData.orders.sort((a, b) => new Date(b.date) - new Date(a.date));
                userData.orders = sortedOrders;
                setUser(userData);
            } catch (error) {
                console.error('Failed to fetch user profile', error);
            }
        };

        fetchUserData();
    }, [userId]);

    if (!user) {
        return <div>Loading...</div>;
    }

    return (
        <>
            <Navigbar />
            <div className='user-profile-container'>
                <h1 className='text-center' style={{ paddingTop: "50px" }}>User Profile</h1>
                <div className='user-profile-addresses'>
                    <h3 style={{ textAlign: 'center', paddingTop: '50px' }}>Addresses</h3>
                    {user.addresses.length > 0 ? (
                        <AddressList user={user} setUser={setUser} redirectPath={`/admin/users/${userId}/profile`} />
                    ) : (
                        <p style={{ textAlign: 'center' }}>No addresses found.</p>
                    )}
                </div>
                <div className='user-profile-orders'>
                    <h3 style={{ textAlign: 'center', paddingTop: '50px' }}>Orders</h3>
                    {user.orders.length > 0 ? (
                        user.orders.map(order => (
                            <OrderCard key={order.id} order={order} className="user-profile-order-card"/>
                        ))
                    ) : (
                        <p>No orders found.</p>
                    )}
                </div>
            </div>
        </>
    );
}

export default UserProfile;
