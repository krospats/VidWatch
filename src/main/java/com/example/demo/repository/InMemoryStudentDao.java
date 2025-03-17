package com.example.demo.repository;

import com.example.demo.model.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryStudentDao {

    private final List<User> users = new ArrayList<>();

    @Autowired
    InMemoryStudentDao(List<User> users) {
        this.addUsers();
    }

    @Builder
    public void addUsers() {
        users.add(User.builder().userName("Abobich").email("maksos@gmail.com").id(1).age(18).build());
        users.add(User.builder().userName("Rambrosich").email("evilMonsta777@mail.ru").id(2).age(20).build());
        users.add(User.builder().userName("Pahom").email("pahom396@yandex.ru").id(3).age(12).build());
    }

    public List<User> findAllUsers() {
        return users;

    }

    public User findUserByUserName(String userName) {
        return users.stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    public User findUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

}
