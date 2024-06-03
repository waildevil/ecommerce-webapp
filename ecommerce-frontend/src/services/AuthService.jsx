import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

class AuthService {
    async login(username, password) {
        const response = await axios.post(API_URL + 'login', {
            username,
            password
        });

        console.log('Login response:', response.data);

        if (response.data.token) {
            const user = {
                token: response.data.token,
                role: this.extractRoleFromToken(response.data.token),
                userId: this.extractUserIdFromToken(response.data.token)
            };
            console.log('Storing user:', user);
            localStorage.setItem('user', JSON.stringify(user));
            console.log('Token stored in local storage');
        }

        return response.data;
    }

    extractRoleFromToken(token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        console.log('Extracted role from token:', payload.role);
        return payload.role;
    }

    extractUserIdFromToken(token) {
        const payload = JSON.parse(atob(token.split('.')[1]));
        console.log('Extracted user ID from token:', payload.userId);
        return payload.userId;
    }

    logout() {
        localStorage.removeItem('user');
        console.log('User logged out, token removed from local storage');
    }

    getCurrentUser() {
        const userStr = localStorage.getItem('user');
        console.log('Retrieved user from local storage:', userStr);
        if (userStr) {
            return JSON.parse(userStr);
        }
        return null;
    }
}

export default new AuthService();
