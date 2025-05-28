import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Spinner, Alert, Button } from 'react-bootstrap';
import VideoList from '../components/VideoList';
import { getUserById } from '../services/userService';
import { getVideos } from '../services/videoService';

const UserPage = () => {
    const { userId } = useParams();
    const navigate = useNavigate();
    const [user, setUser] = useState(null);
    const [videos, setVideos] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                console.log(`Получение пользователя с ID: ${userId}`);
                const userData = await getUserById(userId);
                if (!userData) {
                    throw new Error('Пользователь не найден');
                }
                setUser({
                    id: Number(userData.id),
                    userName: userData.name || `Пользователь ${userData.id}`,
                    email: userData.email || 'Электронная почта не указана'
                });

                const userVideos = (userData.videos || []).map(video => ({
                    ...video,
                    id: Number(video.id),
                    author: userData.name || `Пользователь ${userData.id}`
                }));
                console.log('Видео из userData:', userVideos);
                setVideos(userVideos);
                setLoading(false);
            } catch (err) {
                console.error('Ошибка получения данных пользователя:', err);
                setError(err.message);
                setLoading(false);
            }
        };

        fetchUserData();
    }, [userId]);

    if (loading) {
        return (
            <div className="container text-center mt-5">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Загрузка...</span>
                </Spinner>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container">
                <Alert variant="danger">Ошибка: {error}</Alert>
            </div>
        );
    }

    return (
        <div className="container animate-fade">
            <h2 className="mb-4">Профиль пользователя: {user.userName}</h2>
            <div className="mb-4">
                <h5>Информация о пользователе</h5>
                <p><strong>Имя:</strong> {user.userName}</p>
                <p><strong>Электронная почта:</strong> {user.email}</p>
            </div>
            <h5>Видео пользователя</h5>
            <VideoList
                videos={videos}
                users={[user]}
                showAddButton={false}
            />
            <div className="mt-3">
                <Button variant="secondary" onClick={() => navigate('/')}>
                    Назад к видео
                </Button>
            </div>
        </div>
    );
};

export default UserPage;