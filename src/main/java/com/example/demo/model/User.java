package com.example.demo.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class User {

    @Getter
    private String userName;

    @Getter
    private String email;

    private String password;

    private int age;


}
