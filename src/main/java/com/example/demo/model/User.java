package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
@Table(name = "users")
public class User {
    public User() {}

    @Id // Указываем, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения
    private Long id;

    @Setter
    @Column(name = "user_name", nullable = false) // Указываем имя столбца и что он не может быть null
    private String userName;

    @Column(name = "age")
    @Setter
    private int age;


    @Column(name = "email", nullable = false) // Указываем имя столбца и что он не может быть null
    @Setter
    private String email;


    @Column(name = "password", nullable = false) // Указываем имя столбца и что он не может быть null
    @Setter
    private String password;



}
