package com.example.demo.service;

import com.example.demo.dto.VideoDto;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.model.Video;
import com.example.demo.model.User;
import com.example.demo.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CacheService cacheService;

    @Mock
    private Logger logger;

    @InjectMocks
    private VideoService videoService;

    @Test
    void getVideoById_WhenVideoInCache_ShouldReturnCachedVideo() {
        // Arrange
        Long videoId = 1L;
        String cacheKey = "video_" + videoId;
        Video video = createTestVideo(videoId);
        VideoDto cachedVideoDto = new VideoDto(video);
        cachedVideoDto.setId(videoId);

        when(cacheService.get(eq(cacheKey), eq(VideoDto.class)))
                .thenReturn(Optional.of(cachedVideoDto));

        // Act
        VideoDto result = videoService.getVideoById(videoId);

        // Assert
        assertNotNull(result);
        assertEquals(videoId, result.getId());
        verify(cacheService, times(1)).get(cacheKey, VideoDto.class);
        verify(videoRepository, never()).findById(any());
    }

    @Test
    void getVideoById_WhenVideoNotInCacheButInDB_ShouldReturnVideoAndCacheIt() {
        // Arrange
        Long videoId = 1L;
        String cacheKey = "video_" + videoId;
        Video video = createTestVideo(videoId);
        VideoDto expectedDto = new VideoDto(video);

        when(cacheService.get(eq(cacheKey), eq(VideoDto.class)))
                .thenReturn(Optional.empty());
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));

        // Используем any() для аргументов, так как точное значение VideoDto может отличаться
        doNothing().when(cacheService).put(eq(cacheKey), any(VideoDto.class), eq(VideoDto.class));

        // Act
        VideoDto result = videoService.getVideoById(videoId);

        // Assert
        assertNotNull(result);
        assertEquals(videoId, result.getId());
        verify(cacheService, times(1)).get(cacheKey, VideoDto.class);
        verify(videoRepository, times(1)).findById(videoId);
        verify(cacheService, times(1)).put(eq(cacheKey), any(VideoDto.class), eq(VideoDto.class));
    }

    @Test
    void getVideoById_WhenVideoNotFound_ShouldReturnNull() {
        // Arrange
        Long videoId = 1L;
        String cacheKey = "video_" + videoId;

        when(cacheService.get(eq(cacheKey), eq(VideoDto.class)))
                .thenReturn(Optional.empty());
        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // Act
        VideoDto result = videoService.getVideoById(videoId);

        // Assert
        assertNull(result);
        verify(cacheService, times(1)).get(cacheKey, VideoDto.class);
        verify(videoRepository, times(1)).findById(videoId);
        verify(cacheService, never()).put(any(), any(), any());
    }

    @Test
    void createVideo_ShouldSaveVideoAndClearCache() {
        // Arrange
        Video video = createTestVideo(1L);
        when(videoRepository.save(any(Video.class))).thenReturn(video);
        doNothing().when(cacheService).evict(anyString());

        // Act
        VideoDto result = videoService.createVideo(video);

        // Assert
        assertNotNull(result);
        assertEquals(video.getId(), result.getId());
        verify(videoRepository, times(1)).save(video);
        verify(cacheService, times(1)).evict("users_by_video_" + video.getVideoName());
    }

    @Test
    void getAllVideos_ShouldReturnListOfVideoDtos() {
        // Arrange
        Video video1 = createTestVideo(1L);
        Video video2 = createTestVideo(2L);
        when(videoRepository.findAll()).thenReturn(Arrays.asList(video1, video2));

        // Act
        List<VideoDto> result = videoService.getAllVideos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(video1.getId(), result.get(0).getId());
        assertEquals(video2.getId(), result.get(1).getId());
        verify(videoRepository, times(1)).findAll();
    }

    @Test
    void updateVideo_WhenVideoExists_ShouldUpdateAndClearCache() {
        // Arrange
        Long videoId = 1L;
        Video existingVideo = createTestVideo(videoId);
        Video updatedVideo = createTestVideo(videoId);
        updatedVideo.setVideoName("New Name");
        updatedVideo.setViews(1000);

        when(videoRepository.findById(videoId)).thenReturn(Optional.of(existingVideo));
        when(videoRepository.save(any(Video.class))).thenReturn(updatedVideo);
        doNothing().when(cacheService).evict(anyString());

        // Act
        VideoDto result = videoService.updateVideo(videoId, updatedVideo);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getVideoName());
        assertEquals(1000, result.getViews());
        verify(videoRepository, times(1)).findById(videoId);
        verify(videoRepository, times(1)).save(existingVideo);
        verify(cacheService, times(1)).evict("video_" + videoId);
        verify(cacheService, times(1)).evict("users_by_video_" + updatedVideo.getVideoName());
    }

    @Test
    void updateVideo_WhenVideoNotFound_ShouldThrowException() {
        // Arrange
        Long videoId = 1L;
        Video updatedVideo = createTestVideo(videoId);

        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> videoService.updateVideo(videoId, updatedVideo));
        verify(videoRepository, times(1)).findById(videoId);
        verify(videoRepository, never()).save(any());
        verify(cacheService, never()).evict(any());
    }

    @Test
    void deleteVideo_WhenVideoExists_ShouldDeleteAndClearCache() {
        // Arrange
        Long videoId = 1L;
        Video video = createTestVideo(videoId);

        when(videoRepository.findById(videoId)).thenReturn(Optional.of(video));
        doNothing().when(videoRepository).deleteById(videoId);
        doNothing().when(cacheService).evict(anyString());

        // Act
        videoService.deleteVideo(videoId);

        // Assert
        verify(videoRepository, times(1)).findById(videoId);
        verify(videoRepository, times(1)).deleteById(videoId);
        verify(cacheService, times(1)).evict("video_" + videoId);
        verify(cacheService, times(1)).evict("users_by_video_" + video.getVideoName());
    }

    @Test
    void deleteVideo_WhenVideoNotFound_ShouldThrowNotFoundException() {
        // Arrange
        Long videoId = 1L;

        when(videoRepository.findById(videoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> videoService.deleteVideo(videoId));
        verify(videoRepository, times(1)).findById(videoId);
        verify(videoRepository, never()).deleteById(any());
        verify(cacheService, never()).evict(any());
    }

    private Video createTestVideo(Long id) {
        Video video = new Video();
        video.setId(id);
        video.setVideoName("Test Video");
        video.setReleaseDate("2023-01-01");
        video.setViews(100);
        video.setDuration(120);
        video.setUrl("http://test.com/video");
        video.setLikes(50);
        video.setDislikes(2);
        video.setUser(new User());
        return video;
    }
}