import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Замените на ваш базовый URL
    headers: {
        'Content-Type': 'application/json',
    },
});

// Перехватчик для обработки ошибок
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            console.error('API Error:', error.response.data);
            return Promise.reject(error.response.data);
        }
        console.error('API Error:', error.message);
        return Promise.reject(error.message);
    }
);

export default api;