package com.example.demo.repository;

import com.example.demo.model.VisitCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface VisitCounterRepository extends JpaRepository<VisitCounter, String> {

    @Transactional
    @Modifying
    @Query("UPDATE VisitCounter v SET v.count = v.count + 1 WHERE v.url = :url")
    void incrementCounter(String url);
}