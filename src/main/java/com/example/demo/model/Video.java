package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "video")
@Getter
@Setter
public class Video {

    @Id // Указываем, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения
    private Long id;

    @Column(name = "video_name", nullable = false) // Указываем имя столбца и что он не может быть null
    private String videoName;

    @Column(name = "release_date", nullable = false)
    private String releaseDate;


    @Column(name = "views", nullable = false) // Указываем имя столбца и что он не может быть null
    private int views;

    @Column(name = "duration") // Указываем имя столбца и что он не может быть null
    private int duration;

    @Column(name = "url", nullable = false) // Указываем имя столбца и что он не может быть null
    private String url;



    @Column(name = "likes", nullable = false) // Указываем имя столбца и что он не может быть null
    private int likes;

    @Column(name = "dislikes") // Указываем имя столбца и что он не может быть null
    private int dislikes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    @Getter
    @Setter
    private Long userId;
}







