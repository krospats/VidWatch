package com.example.demo.controller;

import com.example.demo.service.VisitCounterService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/visits")
public class VisitCounterController {
    private final VisitCounterService visitCounterService;

    @Autowired
    public VisitCounterController(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @PostMapping("/count")
    public ResponseEntity<Void> countVisit(@RequestParam String url) {
        visitCounterService.incrementCounter(url);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getVisitCount(@RequestParam String url) {
        return ResponseEntity.ok(visitCounterService.getCount(url));
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Long>> getAllVisitCounts() {
        return ResponseEntity.ok(visitCounterService.getAllCounts());
    }
}