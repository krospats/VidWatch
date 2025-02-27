package com.example.demo.repository;

import com.example.demo.model.User;
import lombok.Builder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryStudentDao {


    private final List<User> users = new ArrayList<>();

    @Builder
    public void addUsers() {


        users.add(User.builder().userName("Abobich").email("maksos@gmail.com").age(18).build());
        users.add(User.builder().userName("Rambrosich").email("evilMonsta777@mail.ru").age(20).build());
        users.add(User.builder().userName("PahomLegich").email("pahom396@zandex.ru").age(12).build());
    }

    public List<User> findAllUsers() {
        addUsers();
        return users;

    }

    public User findUserByUserName(String userName) {
        addUsers();
        return users.stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    public User findUserByUserEmail(String email) {
        addUsers();
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }


}
