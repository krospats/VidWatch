package com.example.demo.dto;

import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов")
    private String name;

    private String email;


    @Min(value = 1, message = "Возраст должен быть не менее 10 лет")
    @Max(value = 120, message = "Возраст должен быть не более 120 лет")
    private Integer age;

    private List<VideoDto> videos;

    public UserDto(User user) {
        this.setId(user.getId());
        this.setName(user.getUserName());
        this.setEmail(user.getEmail());
        this.setAge(user.getAge());

        if (user.getVideos() != null) {
            List<Video> videoList = user.getVideos();
            List<VideoDto> videoDtos = videoList.stream()
                    .map(VideoDto::new)
                    .toList();
            this.setVideos(videoDtos);
        }
    }

}

