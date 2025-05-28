export const getVideos = async () => {
    const response = await fetch('http://localhost:8080/api/videos');
    if (!response.ok) throw new Error('Failed to fetch videos');
    return response.json();
};

export const createVideo = async (videoData) => {
    console.log('Creating video with data:', videoData);
    try {
        const response = await fetch('http://localhost:8080/api/videos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(videoData),
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Server error response:', errorData);
            throw new Error(errorData.message || 'Failed to create video');
        }

        const result = await response.json();
        console.log('Video created successfully:', result);
        return result;
    } catch (error) {
        console.error('Error in createVideo:', error);
        throw error;
    }
};

export const updateVideo = async (id, videoData) => {
    console.log(`Updating video ${id} with data:`, videoData);
    try {
        const response = await fetch(`http://localhost:8080/api/videos/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(videoData),
        });

        if (!response.ok) {
            const errorData = await response.json();
            console.error('Server error response:', errorData);
            throw new Error(errorData.message || 'Failed to update video');
        }

        const result = await response.json();
        console.log('Video updated successfully:', result);
        return result;
    } catch (error) {
        console.error('Error in updateVideo:', error);
        throw error;
    }
};

export const deleteVideo = async (id) => {
    const response = await fetch(`http://localhost:8080/api/videos/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) throw new Error('Failed to delete video');
};

export const getUsersByVideoName = async (videoName) => {
    console.log(`Fetching users for video name: ${videoName}`);
    const response = await fetch(`http://localhost:8080/api/videos/${videoName}/users`);
    if (!response.ok) throw new Error(`Failed to fetch users for video ${videoName}`);
    const users = await response.json();
    console.log(`Users for video ${videoName}:`, users);
    return users;
};