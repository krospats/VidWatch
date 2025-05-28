import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Spinner, Alert, Table, Button } from 'react-bootstrap';
import { getUsers } from '../services/userService';

const UsersPage = () => {
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const userData = await getUsers();
                const mappedUsers = userData.map(user => ({
                    id: Number(user.id),
                    userName: user.userName || user.name || `User ${user.id}`
                }));
                setUsers(mappedUsers);
                setLoading(false);
            } catch (err) {
                console.error('Error fetching users:', err);
                setError(err.message);
                setLoading(false);
            }
        };
        fetchUsers();
    }, []);

    if (loading) {
        return (
            <div className="container text-center mt-5">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container">
                <Alert variant="danger">Error: {error}</Alert>
            </div>
        );
    }

    return (
        <div className="container animate-fade">
            <h2 className="mb-4">Пользователи</h2>
            <Table striped hover responsive className="align-middle">
                <thead>
                <tr>
                    <th>№</th>
                    <th>Пользователь</th>
                    <th>Действия</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user, index) => (
                    <tr key={user.id}>
                        <td>{index + 1}</td>
                        <td>{user.userName}</td>
                        <td>
                            <Button
                                variant="outline-primary"
                                size="sm"
                                onClick={() => navigate(`/users/${user.id}`)}
                            >
                                Профиль
                            </Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </div>
    );
};

export default UsersPage;