import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Spinner, Alert, Modal } from 'react-bootstrap';
import VideoList from '../components/VideoList';
import VideoForm from '../components/VideoForm';
import { getVideos, deleteVideo, createVideo, updateVideo } from '../services/videoService';
import { getUsers } from '../services/userService';

const VideosPage = () => {
    const [videos, setVideos] = useState([]);
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showEditModal, setShowEditModal] = useState(false);
    const [selectedVideo, setSelectedVideo] = useState(null);
    const isSubmittingRef = useRef(false);
    const lastSubmissionTimeRef = useRef(0);
    const submissionQueueRef = useRef(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [videoData, userData] = await Promise.all([getVideos(), getUsers()]);
                console.log('Полученные данные видео из useEffect:', videoData);
                console.log('Полученные данные пользователей из useEffect:', userData);
                const mappedUsers = Array.isArray(userData) ? userData.map(user => ({
                    id: Number(user.id),
                    userName: user.userName || user.name || `Пользователь ${user.id}`
                })) : [];
                console.log('Отображённые пользователи из useEffect:', mappedUsers);
                if (mappedUsers.length === 0) {
                    console.warn('Пользователи не найдены.');
                }
                setUsers(mappedUsers);

                const uniqueVideos = Array.isArray(videoData) ? videoData.filter((video, index, self) =>
                    index === self.findIndex((v) => v.id === video.id)
                ) : [];
                console.log('Уникальные видео из useEffect:', uniqueVideos);

                const videosWithAuthors = uniqueVideos.map(video => {
                    console.log('Обработка видео в useEffect:', video);
                    const author = video.author || 'Неизвестно';
                    console.log('Автор из бэкенда в useEffect:', author);
                    return {
                        ...video,
                        id: Number(video.id),
                        author: author
                    };
                });
                console.log('Видео с авторами из useEffect:', videosWithAuthors);
                setVideos(videosWithAuthors);
                setLoading(false);
            } catch (err) {
                console.error('Ошибка получения данных в useEffect:', err);
                setError(err.message);
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm('Вы уверены, что хотите удалить это видео?')) {

            try {
                await deleteVideo(id);
                setVideos(videos.filter((video) => video.id !== id));
            } catch (err) {
                setError(err.message);
            }
        }
    };

    const handleAddVideo = useCallback(async (videoData) => {
        // Переносим всю логику сюда
        const now = Date.now();


        try {
            isSubmittingRef.current = true;
            lastSubmissionTimeRef.current = now;

            const userId = videoData.userId ? Number(videoData.userId) : null;
            if (!userId) {
                throw new Error('Невозможно добавить видео без ID пользователя.');
            }

            const newVideo = await createVideo(videoData);
            setVideos(prev => [
                ...prev,
                {
                    ...newVideo,
                    id: Number(newVideo.id),
                    author: users.find(u => u.id === userId)?.userName || 'Неизвестно'
                }
            ]);

            return newVideo;
        } catch (err) {
            throw err;
        } finally {
            isSubmittingRef.current = false;
        }
    }, [users]);

    const handleUpdateVideo = async (videoData) => {
        if (isSubmittingRef.current) {
            console.log('Обновление уже в процессе, игнорирование дубликата.');
            return;
        }
        try {
            isSubmittingRef.current = true;
            console.log('Выбранное видео для обновления:', selectedVideo);
            console.log('Обновление видео с ID:', selectedVideo.id);
            console.log('Отправляемые данные видео:', videoData);
            const updatedVideo = await updateVideo(selectedVideo.id, videoData);
            console.log('Видео обновлено на бэкенде:', updatedVideo);
            const userId = videoData.userId ? Number(videoData.userId) : null;
            console.log('Обновление видео с userId:', userId);
            const selectedUser = userId ? users.find(user => user.id === userId) : null;
            const authorName = updatedVideo.author || (selectedUser ? selectedUser.userName : `Неизвестно (User ID: ${userId || 'отсутствует'})`);
            const updatedVideoWithAuthor = {
                ...updatedVideo,
                id: Number(updatedVideo.id),
                author: authorName
            };
            console.log('Обновлённое видео с автором:', updatedVideoWithAuthor);
            setVideos(prevVideos => {
                const newVideos = prevVideos.map(video =>
                    video.id === updatedVideoWithAuthor.id ? { ...updatedVideoWithAuthor } : video
                );
                console.log('Новое состояние видео после обновления:', newVideos);
                return newVideos;
            });
            setShowEditModal(false);
        } catch (err) {
            console.error('Ошибка обновления видео:', err);
            setError(err.message);
        } finally {
            isSubmittingRef.current = false;
        }
    };

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
            <VideoList
                videos={videos}
                users={users}
                onDelete={handleDelete}
                onAddVideo={handleAddVideo}
                onEditVideo={(video) => { setSelectedVideo(video); setShowEditModal(true); }}
            />
            <Modal
                show={showEditModal}
                onHide={() => setShowEditModal(false)}
                centered
                className="animate-fade"


            >
                <Modal.Header closeButton>
                    <Modal.Title>Редактировать видео</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <VideoForm
                        users={users}
                        video={selectedVideo}
                        onSubmit={handleUpdateVideo}
                        isEditing={true}
                        isSubmitting={isSubmittingRef.current} />
                </Modal.Body>
            </Modal>
        </div>
    );
};

export default VideosPage;