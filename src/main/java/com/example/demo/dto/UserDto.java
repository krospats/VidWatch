package com.example.demo.dto;

import com.example.demo.model.User;
import com.example.demo.model.Video;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<VideoDto> videos;

    public UserDto(User user) {
        this.setId(user.getId());
        this.setName(user.getUserName());
        this.setEmail(user.getEmail());

        // Маппинг списка видео
        if (user.getVideos() != null) {
            List<Video> videoList = user.getVideos();
            List<VideoDto> videoDtos = videoList.stream()
                    .map(VideoDto::new)
                    .toList();
            this.setVideos(videoDtos);
        }
    }
}

