import React, { useState, useEffect } from 'react';
import { Form, Button, Row, Col } from 'react-bootstrap';

const VideoForm = ({ users, onSubmit, isEditing, isSubmitting }) => {
    const [videoData, setVideoData] = useState({
        videoName: '',
        url: '',
        duration: '',
        releaseDate: '',
        userId: ''
    });

    useEffect(() => {
        if (isEditing && isSubmitting) {
            setVideoData({
                videoName: '',
                url: '',
                duration: '',
                releaseDate: '',
                userId: ''
            });
        }
    }, [isEditing, isSubmitting]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setVideoData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(videoData);
    };

    return (
        <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="videoName">
                <Form.Label>Название видео</Form.Label>
                <Form.Control
                    type="text"
                    name="videoName"
                    value={videoData.videoName}
                    onChange={handleChange}
                    required
                />
            </Form.Group>
            <Form.Group className="mb-3" controlId="url">
                <Form.Label>ссылка на видео</Form.Label>
                <Form.Control
                    type="text"
                    name="url"
                    value={videoData.url}
                    onChange={handleChange}
                    required
                />
            </Form.Group>
            <Form.Group className="mb-3" controlId="duration">
                <Form.Label>Продолжительность (сек)</Form.Label>
                <Form.Control
                    type="number"
                    name="duration"
                    value={videoData.duration}
                    onChange={handleChange}
                    required
                />
            </Form.Group>
            <Form.Group className="mb-3" controlId="releaseDate">
                <Form.Label>Дата выпуска</Form.Label>
                <Form.Control
                    type="date"
                    name="releaseDate"
                    value={videoData.releaseDate}
                    onChange={handleChange}
                    required
                />
            </Form.Group>
            <Form.Group className="mb-3" controlId="userId">
                <Form.Label>ID пользователя</Form.Label>
                <Form.Control
                    as="select"
                    name="userId"
                    value={videoData.userId}
                    onChange={handleChange}
                    required
                >
                    <option value="">Выберите пользователя</option>
                    {users.map(user => (
                        <option key={user.id} value={user.id}>{user.userName}</option>
                    ))}
                </Form.Control>
            </Form.Group>
            <Button variant="primary" type="submit" disabled={isSubmitting}>
                {isEditing ? 'Обновить' : 'Создать'}
            </Button>
        </Form>
    );
};

export default VideoForm;