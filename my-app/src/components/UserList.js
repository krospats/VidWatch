import React, { useState } from 'react';
import { Table, Button, Modal, Alert } from 'react-bootstrap';
import UserForm from './UserForm';

const UserList = ({ users, onDelete, onAddUser, onEditUser }) => {
    const [showModal, setShowModal] = useState(false);
    const [modalError, setModalError] = useState(null);

    const handleAddUser = async (userData) => {
        try {
            await onAddUser(userData);
            setShowModal(false);
            setModalError(null);
        } catch (err) {
            setModalError(err.message);
        }
    };

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2>User Management</h2>
                <Button variant="primary" onClick={() => setShowModal(true)}>
                    Add New User
                </Button>
            </div>
            <div className="animate-fade">
                <Table striped hover responsive className="align-middle">
                    <thead>
                    <tr>
                        <th style={{ minWidth: '150px' }}>Имя</th>
                        <th style={{ minWidth: '200px' }}>Email</th>
                        <th style={{ minWidth: '100px' }}>Возраст</th>
                        <th width="180">Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {Array.isArray(users) ? users.map((user) => (
                        <tr key={user.id}>
                            <td>{user.userName || '[No Name]'}</td>
                            <td>{user.email || '[No Email]'}</td>
                            <td>{user.age || '[No Age]'}</td>
                            <td>
                                <div className="d-flex gap-2">
                                    <Button
                                        variant="outline-primary"
                                        size="sm"
                                        onClick={() => onEditUser(user)}
                                    >
                                        Редактировать
                                    </Button>
                                    <Button
                                        variant="outline-danger"
                                        size="sm"
                                        onClick={() => onDelete(user.id)}
                                    >
                                        Удалить
                                    </Button>
                                </div>
                            </td>
                        </tr>
                    )) : null}
                    </tbody>
                </Table>
            </div>
            <Modal
                show={showModal}
                onHide={() => setShowModal(false)}
                centered
                className="animate-fade"
            >
                <Modal.Header closeButton>
                    <Modal.Title>Добавить нового пользователя</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {modalError && <Alert variant="danger">{modalError}</Alert>}
                    <UserForm onSubmit={handleAddUser} isEditing={false} />
                </Modal.Body>
            </Modal>
        </>
    );
};

export default UserList;