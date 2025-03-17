package com.example.demo.dto;


import com.example.demo.model.Video;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoDto {
    private Long id;
    private String videoName;
    private String url;
    private int duration;
    private String releaseDate;
    private int views;
    private int likes;
    private int dislikes;
    private String author;


    public VideoDto(Video video) {
        this.setId(video.getId());
        this.setVideoName(video.getVideoName());
        this.setUrl(video.getUrl());
        this.setDuration(video.getDuration());
        this.setReleaseDate(video.getReleaseDate());
        this.setViews(video.getViews());
        this.setLikes(video.getLikes());
        this.setDislikes(video.getDislikes());
        this.setAuthor(video.getUser().getUserName());
    }
}