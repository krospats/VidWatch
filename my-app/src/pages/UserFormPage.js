import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import UserForm from '../components/UserForm';
import { getUser, createUser, updateUser } from '../services/userService';

const UserFormPage = ({ isEditing = false }) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [user, setUser] = useState(null);

    useEffect(() => {
        if (isEditing && id) {
            const fetchUser = async () => {
                try {
                    const data = await getUser(id);
                    setUser(data);
                } catch (err) {
                    console.error(err);
                }
            };
            fetchUser();
        }
    }, [id, isEditing]);

    const handleSubmit = async (userData) => {
        try {
            if (isEditing) {
                await updateUser(id, userData);
            } else {
                await createUser(userData);
            }
            navigate('/users');
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className="container">
            <div className="form-container animate-fade">
                <h2>{isEditing ? 'Edit User' : 'Create User'}</h2>
                <UserForm user={user} onSubmit={handleSubmit} isEditing={isEditing} />
            </div>
        </div>
    );
};

export default UserFormPage;