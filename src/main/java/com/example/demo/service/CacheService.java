package com.example.demo.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class CacheService {
    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);
    private final int maxCacheSize = 100;
    private final Map<String, Object> cache;

    public CacheService() {
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
                boolean shouldRemove = size() > maxCacheSize;
                if (shouldRemove) {
                    logger.info("Evicting LRU cache entry: {}", eldest.getKey());
                }
                return shouldRemove;
            }
        };
    }

    public <T> void put(String key, T value, Class<T> type) {
        if (!type.isInstance(value)) {
            logger.error("Attempt to cache value of wrong type. Key: {}, Expected: {}, Actual: {}",
                    key, type.getSimpleName(), value.getClass().getSimpleName());
            throw new IllegalArgumentException("Invalid type for cache");
        }
        cache.put(key, value);
    }



    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = cache.get(key);
        if (type.isInstance(value)) {
            logger.debug("Cache HIT for key: {} (type: {})", key, type.getSimpleName());
            return Optional.of(type.cast(value));
        }
        logger.debug("Cache MISS for key: {}", key);
        return Optional.empty();
    }

    public synchronized void evict(String key) {
        logger.debug("Evicting cache entry: {}", key);
        cache.remove(key);
        logger.info("Cache size after evict: {}/{}", size(), maxCacheSize);
    }

    public synchronized void clear() {
        logger.info("Clearing entire cache");
        cache.clear();
    }

    public synchronized int size() {
        return cache.size();
    }

    public synchronized int getMaxSize() {
        return maxCacheSize;
    }
}