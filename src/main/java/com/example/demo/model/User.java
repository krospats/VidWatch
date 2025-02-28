package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class User {

    private String userName;
    private String email;
    private String password;
    private int age;

}
