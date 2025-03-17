package com.example.demo.service;

import com.example.demo.dto.VideoDto;
import com.example.demo.model.Video;
import com.example.demo.repository.VideoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

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
    public VideoDto getVideoById(Long id) {
        Optional<Video> video = videoRepository.findById(id);
        return video.map(VideoDto::new).orElse(null);
    }

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