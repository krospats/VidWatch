import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import VideoForm from '../components/VideoForm';
import { getVideo, createVideo, updateVideo } from '../services/videoService';

const VideoFormPage = ({ isEditing = false }) => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [video, setVideo] = useState(null);

    useEffect(() => {
        if (isEditing && id) {
            const fetchVideo = async () => {
                try {
                    const data = await getVideo(id);
                    setVideo(data);
                } catch (err) {
                    console.error(err);
                }
            };
            fetchVideo();
        }
    }, [id, isEditing]);

    const handleSubmit = async (videoData) => {
        try {
            if (isEditing) {
                await updateVideo(id, videoData);
            } else {
                await createVideo(videoData);
            }
            navigate('/videos');
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className="container">
            <div className="form-container animate-fade">
                <h2>{isEditing ? 'Edit Video' : 'Create Video'}</h2>
                <VideoForm video={video} onSubmit={handleSubmit} isEditing={isEditing} />
            </div>
        </div>
    );
};

export default VideoFormPage;