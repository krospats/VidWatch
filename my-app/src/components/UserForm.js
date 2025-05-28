import React from 'react';
import { Form, Button, FloatingLabel } from 'react-bootstrap';

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
            <div className="form-columns">
                <div className="form-column-left">
                    <FloatingLabel controlId="userName" label="Имя пользователя" className="mb-3">
                        <Form.Control
                            type="text"
                            name="userName"
                            value={formData.userName}
                            onChange={handleChange}
                            required
                            placeholder="Имя пользователя"
                        />
                    </FloatingLabel>
                    <FloatingLabel controlId="email" label="Электронная почта" className="mb-3">
                        <Form.Control
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            placeholder="Email"
                        />
                    </FloatingLabel>
                </div>
                <div className="form-column-right">
                    <FloatingLabel controlId="age" label="Возраст" className="mb-3">
                        <Form.Control
                            type="number"
                            name="age"
                            value={formData.age}
                            onChange={handleChange}
                            required
                            min="1"
                            placeholder="Возраст"
                        />
                    </FloatingLabel>
                </div>
            </div>
            <div className="d-flex justify-content-end">
                <Button variant="primary" type="submit" size="lg">
                    {isEditing ? 'Сохранить изменения' : 'Добавить пользователя'}
                </Button>
            </div>
        </Form>
    );
};

export default UserForm;