

export const createUser = async (userData) => {
    const response = await fetch('http://localhost:8080/api/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
    });
    if (!response.ok) throw new Error('Failed to create user');
    return response.json();
};

export const updateUser = async (id, userData) => {
    console.log(`Sending PUT request to update user ${id}:`, userData);
    const response = await fetch(`http://localhost:8080/api/users/${id}`, {
        method: 'PUT', // Ensure PUT is used for updates
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData),
    });
    if (!response.ok) throw new Error('Failed to update user');
    const updatedUser = await response.json();
    console.log(`Updated user response:`, updatedUser);
    return updatedUser;
};

export const deleteUser = async (id) => {
    const response = await fetch(`http://localhost:8080/api/users/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete user');
};






const API_URL = 'http://localhost:8080/api/users';

const getUsers = async () => {
    try {
        console.log('Fetching all users from:', API_URL);
        const response = await fetch(API_URL);
        if (!response.ok) {
            const errorText = await response.text();
            console.error('Failed to fetch users, status:', response.status, 'response:', errorText);
            throw new Error('Failed to fetch users');
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching users:', error);
        throw error;
    }
};

const getUserById = async (userId) => {
    try {
        console.log(`Fetching user with ID ${userId} from: ${API_URL}/${userId}`);
        const response = await fetch(`${API_URL}/${userId}`);
        if (!response.ok) {
            const errorText = await response.text();
            console.error(`Failed to fetch user with ID ${userId}, status: ${response.status}, response:`, errorText);
            throw new Error(`Failed to fetch user with ID ${userId}`);
        }
        return await response.json();
    } catch (error) {
        console.error(`Error fetching user with ID ${userId}:`, error);
        throw error;
    }
};

export { getUsers, getUserById };