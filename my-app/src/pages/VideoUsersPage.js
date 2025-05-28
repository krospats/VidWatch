import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { Spinner, Alert, Table, Button } from 'react-bootstrap';
import { getUsersByVideoName } from '../services/videoService';

const VideoUsersPage = () => {
    const { videoName } = useParams();
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const data = await getUsersByVideoName(videoName);
                console.log('Fetched users for video:', data);
                setUsers(Array.isArray(data) ? data : []);
                setLoading(false);
            } catch (err) {
                console.error('Error fetching users:', err);
                setError(err.message);
                setLoading(false);
            }
        };

        fetchUsers();
    }, [videoName]);

    if (loading) {
        return (
            <div className="container text-center mt-5">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container">
                <Alert variant="danger">Error: {error}</Alert>
            </div>
        );
    }

    return (
        <div className="container animate-fade">
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h2>Users who interacted with "{videoName}"</h2>
                <Button as={Link} to="/videos" variant="outline-primary">
                    Back to Videos
                </Button>
            </div>
            <Table striped hover responsive className="align-middle">
                <thead>
                <tr>
                    <th>Имя</th>
                    <th>Email</th>
                    <th>Возраст</th>
                </tr>
                </thead>
                <tbody>
                {users.map((user) => (
                    <tr key={user.id}>
                        <td>{user.userName || '[No Name]'}</td>
                        <td>{user.email || '[No Email]'}</td>
                        <td>{user.age || '[No Age]'}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </div>
    );
};

export default VideoUsersPage;