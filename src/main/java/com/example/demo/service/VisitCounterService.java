package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;


@Service
public class VisitCounterService {
    private final ConcurrentHashMap<String, AtomicLong> urlCounters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ReentrantLock> urlLocks = new ConcurrentHashMap<>();

    public void incrementCounter(String url) {
        ReentrantLock lock = urlLocks.computeIfAbsent(url, k -> new ReentrantLock());
        lock.lock();
        try {
            AtomicLong counter = urlCounters.computeIfAbsent(url, k -> new AtomicLong(0));
            counter.incrementAndGet();
        } finally {
            lock.unlock();
        }
    }

    public long getCount(String url) {
        AtomicLong counter = urlCounters.get(url);
        return counter == null ? 0 : counter.get();
    }

    public Map<String, Long> getAllCounts() {
        Map<String, Long> result = new HashMap<>();
        urlCounters.forEach((url, counter) ->
                result.put(url, counter.get()));
        return result;
    }
}