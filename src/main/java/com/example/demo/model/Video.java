package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "video")
@Getter
@Setter
public class Video {

    @Id // Указываем, что это первичный ключ
    @Min(value = 1, message = "Id  не может быть отрицательным")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения
    private Long id;

    @NotBlank
    @Column(name = "video_name", nullable = false) // Указываем имя столбца и что он не может быть null
    private String videoName;

    @Column(name = "release_date", nullable = false)
    private String releaseDate;


    @Column(name = "views", nullable = false)
    @Min(value = 0, message = "Просмотры не могут быть отрицательным")
    private int views;

    @Column(name = "duration") // Указываем имя столбца и что он не может быть null
    @Min(value = 0, message = "Длина  не может быть отрицательным")
    private int duration;

    @NotBlank
    @Column(name = "url", nullable = false) // Указываем имя столбца и что он не может быть null
    private String url;



    @Column(name = "likes", nullable = false) // Указываем имя столбца и что он не может быть null
    @Min(value = 0, message = "Лайки не может быть отрицательным")
    private int likes;

    @Column(name = "dislikes") // Указываем имя столбца и что он не может быть null
    @Min(value = 0, message = "Дизлайки не могут быть отрицательным")
    private int dislikes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    @Getter
    @Setter
    @Min(value = 1, message = "Id не может быть отрицательным")
    private Long userId;
}







