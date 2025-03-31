package com.example.demo.repository;

import com.example.demo.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserAndVideoRepository extends JpaRepository<User, Long> {



    @Query("SELECT u FROM User u JOIN u.videos v WHERE v.videoName LIKE %:videoName%")
    List<User> findUsersByVideoName(@Param("videoName") String videoName);
}