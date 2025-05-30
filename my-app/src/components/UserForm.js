import React from 'react';
import { Form, Button } from 'react-bootstrap';

const UserForm = ({ user, onSubmit, isEditing }) => {
    const [formData, setFormData] = React.useState({
        userName: '',
        email: '',
        age: ''
    });



    React.useEffect(() => {
        if (user) {
            setFormData({
                userName: user.userName || '',
                email: user.email || '',
                age: user.age || ''
            });
        }
    }, [user]);




    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <Form onSubmit={handleSubmit} className="animate-fade">
            <h2 className="mb-4">{isEditing ? 'Редактировать пользователя' : 'Добавить пользователя'}</h2>

            <div className="mb-4">
                <h5>Имя пользователя</h5>
                <Form.Control
                    type="text"
                    name="userName"
                    value={formData.userName}
                    onChange={handleChange}
                    required
                    className="mb-2"
                    placeholder="Введите имя"
                />
            </div>

            <div className="mb-4">
                <h5>Электронная почта</h5>
                <Form.Control
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                    className="mb-2"
                    placeholder="Введите email"
                />
            </div>

            <div className="mb-4">
                <h5>Возраст</h5>
                <Form.Control
                    type="number"
                    name="age"
                    value={formData.age}
                    onChange={handleChange}
                    required
                    min="1"
                    className="mb-2"
                    placeholder="Введите возраст"
                />
            </div>

            <div className="d-flex justify-content-start mt-4">
                <Button
                    variant="primary"
                    type="submit"
                    size="lg"
                    style={{ minWidth: '150px' }}
                >
                    {isEditing ? 'Сохранить изменения' : 'Создать'}
                </Button>
            </div>
        </Form>
    );
};

export default UserForm;