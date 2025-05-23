package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "visit_counters")
public class VisitCounter {
    @Id
    private String url;
    private long count;

    public VisitCounter() {
    }

    public VisitCounter(String url) {
        this.url = url;
        this.count = 0;
    }

}