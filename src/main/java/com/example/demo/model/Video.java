package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;



@Entity
@Table(name = "video")
@Getter
@Setter
public class Video {
    public Video() {}

    @Id // Указываем, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения
    private Long id;

    @Column(name = "video_name", nullable = false) // Указываем имя столбца и что он не может быть null
    private String videoName;

    @Column(name = "release_date", nullable = false)
    private String releaseDate;


    @Column(name = "views", nullable = false) // Указываем имя столбца и что он не может быть null
    private String views;

    @Column(name = "duration") // Указываем имя столбца и что он не может быть null
    private int duration;

    @Column(name = "url", nullable = false) // Указываем имя столбца и что он не может быть null
    private String url;



    @Column(name = "likes", nullable = false) // Указываем имя столбца и что он не может быть null
    private String likes;

    @Column(name = "dislikes") // Указываем имя столбца и что он не может быть null
    private int dislikes;

}






