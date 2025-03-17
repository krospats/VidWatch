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
