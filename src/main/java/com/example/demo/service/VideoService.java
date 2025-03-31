package com.example.demo.service;

import com.example.demo.dto.VideoDto;
import com.example.demo.model.Video;
import com.example.demo.repository.VideoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class VideoService {

    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    private final VideoRepository videoRepository;
    private final CacheService cacheService;

    @Autowired
    public VideoService(VideoRepository videoRepository, CacheService cacheService) {
        this.videoRepository = videoRepository;
        this.cacheService = cacheService;
    }

    public VideoDto getVideoById(Long id) {
        String cacheKey = "video_" + id;

        // Используем новый метод с проверкой типа
        Optional<VideoDto> cachedVideo = cacheService.get(cacheKey, VideoDto.class);
        if (cachedVideo.isPresent()) {
            logger.info("Returning video {} from cache", id);
            return cachedVideo.get();
        }

        logger.debug("Video {} not found in cache, querying database", id);
        Optional<Video> video = videoRepository.findById(id);

        if (video.isPresent()) {
            VideoDto videoDto = new VideoDto(video.get());
            cacheService.put(cacheKey, videoDto, VideoDto.class);
            return videoDto;
        }

        return null;
    }


    // Создание видео


    public VideoDto createVideo(Video video) {
        return new VideoDto(videoRepository.save(video));
    }

    // Получение всех видео
    public List<VideoDto> getAllVideos() {
        List<VideoDto> videoDtos = new ArrayList<>();
        for (Video video : videoRepository.findAll()) {
            videoDtos.add(new VideoDto(video));
        }
        return videoDtos;
    }

    // Получение видео по ID


    // Обновление видео
    public VideoDto updateVideo(Long id, Video videoDetails) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found"));
        video.setVideoName(videoDetails.getVideoName());
        video.setUrl(videoDetails.getUrl());
        video.setDuration(videoDetails.getDuration());
        video.setReleaseDate(videoDetails.getReleaseDate());
        video.setViews(videoDetails.getViews());
        video.setLikes(videoDetails.getLikes());
        video.setDislikes(videoDetails.getDislikes());
        return new VideoDto(videoRepository.save(video));
    }

    // Удаление видео
    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }
}