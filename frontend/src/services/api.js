import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

export const authAPI = {
    register: async (username, password, email) => {
        const response = await axios.post(`${API_BASE}/register`, {
            username,
            password,
            email
        });
        return response.data;
    },

    login: async (username, password) => {
        const response = await axios.post(`${API_BASE}/login`, {
            username,
            password
        });
        return response.data;
    },

    getUser: async (username) => {
        const response = await axios.get(`${API_BASE}/user?username=${username}`);
        return response.data;
    }
};