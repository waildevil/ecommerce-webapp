import axios from 'axios';
import AuthService from './AuthService';


const setup = () => {
    axios.interceptors.request.use(
        config => {
            const user = AuthService.getCurrentUser();
            if (user && user.token) {
                config.headers['Authorization'] = 'Bearer ' + user.token;
            }
            return config;
        },
        error => {
            return Promise.reject(error);
        }
    );

    axios.interceptors.response.use(
        response => {
            return response;
        },
        error => {
            if (error.response && error.response.status === 401) {
                AuthService.logout();
                window.location.reload();
            }
            return Promise.reject(error);
        }
    );
};

export default setup;
