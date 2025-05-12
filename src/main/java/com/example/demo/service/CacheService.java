package com.example.demo.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
public class CacheService {
    private static final int MAX_CACHE_SIZE = 100;
    private final Map<String, Object> cache;
    private final Logger logger;

    public CacheService() {
        this(LoggerFactory.getLogger(CacheService.class));
    }

    // Конструктор для тестирования
    protected CacheService(Logger logger) {
        this.logger = logger;
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
                boolean shouldRemove = size() > getMaxSize();
                if (shouldRemove) {
                    logger.info("Evicting LRU cache entry");
                }
                return shouldRemove;
            }
        };
    }

    public <T> void put(String key, T value, Class<T> type) {
        cache.put(key, value);
    }



    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = cache.get(key);
        if (type.isInstance(value)) {
            logger.debug("Cache HIT");
            return Optional.of(type.cast(value));
        }
        logger.debug("Cache MISS");
        return Optional.empty();
    }


    public synchronized int size() {
        return cache.size();
    }

    public synchronized int getMaxSize() {
        return MAX_CACHE_SIZE;
    }

    public void evict(String key) {
        cache.remove(key);
    }

}