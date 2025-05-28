import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import UsersPage from './pages/UsersPage';
import VideosPage from './pages/VideosPage';
import VideoUsersPage from './pages/VideoUsersPage';
import UserPage from './pages/UserPage';


function App() {
    return (
        <>
            <Navbar />
            <Routes>
                <Route path="/" element={<VideosPage />} />
                <Route path="/videos" element={<VideosPage />} />
                <Route path="/users" element={<UsersPage />} />
                <Route path="/videos/:videoName/users" element={<VideoUsersPage />} />
                <Route path="/users/:userId" element={<UserPage />} />
            </Routes>
        </>
    );
}

export default App;