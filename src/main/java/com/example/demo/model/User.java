package com.example.demo.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id // Указываем, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическая генерация значения
    @Min(value = 1, message = "Id  не может быть отрицательным")
    private Long id;

    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя должно быть от 3 до 50 символов")
    @Column(name = "user_name", nullable = false) // Указываем имя столбца и что он не может быть null
    private String userName;

    @Min(value = 10, message = "Возраст должен быть не менее 10 лет")
    @Max(value = 120, message = "Возраст должен быть не более 120 лет")
    @Column(name = "age")
    private int age;

    @Email(message = "Некорректный email")
    @Column(name = "email", nullable = false) // Указываем имя столбца и что он не может быть null
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Пароль должен содержать минимум 8 символов, буквы и цифры")
    @Column(name = "password", nullable = false) // Указываем имя столбца и что он не может быть null
    private String password;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Video> videos;



}
