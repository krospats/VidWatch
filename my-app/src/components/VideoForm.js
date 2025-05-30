import React, { useState, useEffect } from 'react';
import { Form, Button } from 'react-bootstrap';

const VideoForm = ({ users, video, onSubmit, isEditing, isSubmitting }) => {
    const [videoData, setVideoData] = useState({
        videoName: '',
        url: '',
        duration: '',
        releaseDate: '',
        userId: ''
    });

    useEffect(() => {
        if (video) {
            const formattedDate = video.releaseDate
                ? new Date(video.releaseDate).toISOString().split('T')[0]
                : '';

            setVideoData({
                videoName: video.videoName || '',
                url: video.url || '',
                duration: video.duration || '',
                releaseDate: formattedDate,
                userId: video.userId || ''
            });
        } else {
            setVideoData({
                videoName: '',
                url: '',
                duration: '',
                releaseDate: '',
                userId: ''
            });
        }
    }, [video]);

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
            <h2 className="mb-4">{isEditing ? 'Редактировать видео' : 'Добавить видео'}</h2>

            <Form.Group className="mb-3" controlId="videoName">
                <Form.Label column="1">Название видео</Form.Label>
                <Form.Control
                    type="text"
                    name="videoName"
                    value={videoData.videoName}
                    onChange={handleChange}
                    required
                />
            </Form.Group>

            <Form.Group className="mb-3" controlId="url">
                <Form.Label column="2">Ссылка на видео</Form.Label>
                <Form.Control
                    type="text"
                    name="url"
                    value={videoData.url}
                    onChange={handleChange}
                    required
                />
            </Form.Group>

            <Form.Group className="mb-3" controlId="duration">
                <Form.Label column="3">Продолжительность (сек)</Form.Label>
                <Form.Control
                    type="number"
                    name="duration"
                    value={videoData.duration}
                    onChange={handleChange}
                    required
                    min="1"
                />
            </Form.Group>

            <Form.Group className="mb-3" controlId="releaseDate">
                <Form.Label column="4">Дата выпуска</Form.Label>
                <Form.Control
                    type="date"
                    name="releaseDate"
                    value={videoData.releaseDate}
                    onChange={handleChange}
                    required
                />
            </Form.Group>

            <Form.Group className="mb-3" controlId="userId">
                <Form.Label column="5">Имя пользователя</Form.Label>
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

            <div className="d-flex justify-content-start">
                <Button
                    variant="primary"
                    type="submit"
                    disabled={isSubmitting}
                    style={{ minWidth: '120px' }}
                >
                    {isSubmitting ? (
                        <span className="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                    ) : isEditing ? (
                        'Обновить'
                    ) : (
                        'Создать'
                    )}
                </Button>
            </div>
        </Form>
    );
};

export default VideoForm;