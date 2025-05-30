import React, { useState } from 'react';
import { Table, Button, Badge, Modal, Alert } from 'react-bootstrap';
import VideoForm from './VideoForm';

const VideoList = ({ videos, users, onDelete, onAddVideo, onEditVideo, showAddButton = true }) => {
    const [showModal, setShowModal] = useState(false);
    const [modalError, setModalError] = useState(null);

    const formatDuration = (seconds) => {
        const hours = Math.floor(seconds / 3600);
        const remainingSeconds = seconds % 3600;
        const mins = Math.floor(remainingSeconds / 60);
        const secs = remainingSeconds % 60;
        if (hours > 0) {
            return `${hours}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
        }
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    };

    const handleAddVideo = async (videoData) => {
        try {
            console.log('Creating video with data:', videoData);
            onAddVideo(videoData);
            setShowModal(false);
            setModalError(null);
        } catch (err) {
            setModalError(err.message);
        }
    };

    return (
        <>
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2>Менеджмент видео</h2>
                {showAddButton && (
                    <Button variant="primary" onClick={() => setShowModal(true)}>
                        Добавить видео
                    </Button>
                )}
            </div>
            <div className="animate-fade">
                <Table striped hover responsive className="align-middle">
                    <thead>
                    <tr>
                        <th>Название</th>
                        <th>Автор</th>
                        <th>Длительность</th>
                        {onDelete && onEditVideo && <th  className="text-center w-25">Действия</th>}
                    </tr>
                    </thead>
                    <tbody>
                    {videos.map((video) => (
                        <tr key={video.id}>
                            <td>
                                <strong>{video.videoName}</strong>
                            </td>
                            <td>{video.author || 'Unknown'}</td>
                            <td>
                                <Badge bg="secondary">{formatDuration(video.duration)}</Badge>
                            </td>
                            {onDelete && onEditVideo && (
                                <td>
                                    <div  style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                        <Button
                                            variant="outline-warning"
                                            size="sm"
                                            onClick={() => onEditVideo(video)}
                                        >
                                            Редактировать
                                        </Button>
                                        <Button
                                            variant="outline-danger"
                                            size="sm"
                                            onClick={() => onDelete(video.id)}
                                        >
                                            Удалить
                                        </Button>
                                    </div>
                                </td>
                            )}
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </div>
            {showAddButton && (
                <Modal
                    show={showModal}
                    onHide={() => setShowModal(false)}
                    centered
                    className="animate-fade"
                >
                    <Modal.Header closeButton>
                        <Modal.Title>Добавить новое видео</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {modalError && <Alert variant="danger">{modalError}</Alert>}
                        <VideoForm users={users} onSubmit={handleAddVideo} isEditing={false} setFormSubmitting={() => {}} />
                    </Modal.Body>
                </Modal>
            )}
        </>
    );
};

export default VideoList;