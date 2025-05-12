package com.example.demo.dto;


import com.example.demo.model.Video;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "DTO для видео")
public class VideoDto {

    @Schema(description = "ID видео", example = "1")
    private Long id;

    @Schema(description = "название видео", example = "day at the zoo")
    private String videoName;

    @Schema(description = "ссылка на видео", example = "https://youtu.be/dQw4w9WgXcQ?si=PtmP1JnFAayEuq8U")
    private String url;

    @Schema(description = "длина видео в секундах", example = "1650")
    private int duration;

    @Schema(description = "дата выхода(yyyy-mm-dd)", example = "2024-04-04")
    private String releaseDate;

    @Schema(description = "количество просмотров", example = "250")
    private int views;

    @Schema(description = "количество лайков", example = "51")
    private int likes;

    @Schema(description = "количество дизлайков", example = "51")
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