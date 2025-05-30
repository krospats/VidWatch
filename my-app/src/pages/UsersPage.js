import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {Spinner, Table, Button, Modal, Alert} from 'react-bootstrap';
import { getUsers, createUser, updateUser, deleteUser } from '../services/userService';
import UserForm from '../components/UserForm';

const UsersPage = () => {
    const navigate = useNavigate();
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);
    const [showCreateModal, setShowCreateModal] = useState(false);

    const [editingUser, setEditingUser] = useState(null);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            setLoading(true);
            const userData = await getUsers();
            const mappedUsers = userData.map(user => ({
                id: Number(user.id),
                userName: user.userName || user.name || `User ${user.id}`,
                email: user.email,
                age: Number(user.age)
            }));
            setUsers(mappedUsers);
        } catch (err) {
            setError('Ошибка загрузки пользователей');
            console.error('Error fetching users:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleCreateUser = async (formData) => {
        try {
            setLoading(true);
            setError(null);
            await createUser(formData);
            setSuccess('Пользователь успешно создан');
            setShowCreateModal(false);
            await fetchUsers();
        } catch (err) {
            setError('Ошибка создания пользователя');
            console.error('Error creating user:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleEditUser = (user) => {
        setEditingUser(user);
        setShowCreateModal(true);
    };

    const handleUpdateUser = async (formData) => {
        try {
            setLoading(true);
            await updateUser(editingUser.id, formData);
            setSuccess('Пользователь успешно обновлен');
            setShowCreateModal(false);
            setEditingUser(null);
            await fetchUsers();
        } catch (err) {
            setError('Ошибка обновления пользователя');
            console.error('Error updating user:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteUser = async (userId) => {
        if (window.confirm('Вы уверены, что хотите удалить этого пользователя?')) {
            try {
                setLoading(true);
                await deleteUser(userId);
                setSuccess('Пользователь успешно удален');
                await fetchUsers();
            } catch (err) {
                setError('Ошибка удаления пользователя');
                console.error('Error deleting user:', err);
            } finally {
                setLoading(false);
            }
        }
    };

    if (loading && users.length === 0) {
        return (
            <div className="container text-center mt-5">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        );
    }

    return (
        <div className="container animate-fade">
            {error && <Alert variant="danger" onClose={() => setError(null)} dismissible>{error}</Alert>}
            {success && <Alert variant="success" onClose={() => setSuccess(null)} dismissible>{success}</Alert>}
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Пользователи</h2>
                <Button
                    variant="primary"
                    onClick={() => {
                        setEditingUser(null);
                        setShowCreateModal(true);
                    }}
                    disabled={loading}
                >
                    Добавить пользователя
                </Button>
            </div>





            <Table striped hover responsive className="align-middle">
                <thead>
                <tr>
                    <th>№</th>
                    <th className="text-start">Пользователь</th>
                    <th className="text-center w-10">Действия</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user, index) => (
                    <tr key={user.id}>
                        <td>{index + 1}</td>
                        <td className="text-start">{user.userName}</td>
                        <td className="text-center w-25">
                            <Button
                                variant="outline-primary"
                                size="sm"
                                onClick={() => navigate(`/users/${user.id}`)}
                                className="me-2"
                                disabled={loading}
                            >
                                Профиль
                            </Button>
                            <Button
                                variant="outline-warning"
                                size="sm"
                                onClick={() => handleEditUser(user)}
                                className="me-2"
                                disabled={loading}
                            >
                                Редактировать
                            </Button>
                            <Button
                                variant="outline-danger"
                                size="sm"
                                onClick={() => handleDeleteUser(user.id)}
                                disabled={loading}
                            >
                                Удалить
                            </Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>

            <Modal show={showCreateModal} onHide={() => {
                setShowCreateModal(false);
                setEditingUser(null);
            }}>
                <Modal.Header closeButton>
                    <Modal.Title>
                        {editingUser ? 'Редактировать пользователя' : 'Добавить пользователя'}
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <UserForm
                        user={editingUser}
                        onSubmit={editingUser ? handleUpdateUser : handleCreateUser}
                        isEditing={!!editingUser}
                    />
                </Modal.Body>
            </Modal>
        </div>
    );
};

export default UsersPage;