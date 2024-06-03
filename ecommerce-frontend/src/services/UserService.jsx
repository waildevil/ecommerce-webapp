import axios from "axios";

const REST_API_BASE_URL = 'http://localhost:8080/api/users';

export const listUsers = () => axios.get(REST_API_BASE_URL);

export const updateUser = (userId, user) => axios.put(REST_API_BASE_URL + '/updateUser/' + userId, user);

export const createUser = (user) => axios.post(REST_API_BASE_URL + '/createUser', user);

export const getUser = (userId) => axios.get(REST_API_BASE_URL + '/' + userId);

export const getRoles = () => axios.get(REST_API_BASE_URL + "/roles");

export const deleteUser = (userId) => axios.delete(REST_API_BASE_URL + '/deleteUser/' + userId);

export const toggleUserActivation = async (userId) => {
    try {
        const response = await axios.put(`/api/users/toggle-activation/${userId}`);
        return response.data;
    } catch (error) {
        console.error('Error toggling user activation:', error);
        throw error;
    }
};

export const searchUsers = (searchTerm, roleName) => {
    return axios.get(REST_API_BASE_URL + '/search', {
        params: {
            searchTerm,
            roleName
        }
    });
};
