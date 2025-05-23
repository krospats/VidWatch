package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    @Mock
    private Logger logger;

    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new CacheService(logger);
    }

    // Тесты функциональности без проверки логгирования
    @Test
    void putAndGet_ShouldWorkCorrectly() {
        String key = "testKey";
        String value = "testValue";

        cacheService.put(key, value, String.class);
        Optional<String> result = cacheService.get(key, String.class);

        assertTrue(result.isPresent());
        assertEquals(value, result.get());
    }



    @Test
    void get_ShouldReturnEmptyWhenKeyNotFound() {
        Optional<String> result = cacheService.get("nonexistent", String.class);
        assertFalse(result.isPresent());
    }

    @Test
    void evict_ShouldRemoveEntry() {
        String key = "testKey";
        cacheService.put(key, "value", String.class);
        cacheService.evict(key);
        assertFalse(cacheService.get(key, String.class).isPresent());
    }

    // Отдельные тесты для логгирования
    @Test
    void get_ShouldLogCacheMissWhenKeyNotFound() {
        cacheService.get("nonexistent", String.class);
        verify(logger).debug("Cache MISS");
    }

    @Test
    void get_ShouldLogCacheHitWhenKeyExists() {
        String key = "testKey";
        cacheService.put(key, "value", String.class);
        cacheService.get(key, String.class);
        verify(logger).debug("Cache HIT");
    }

    @Test
    void cache_ShouldEvictOldestWhenFull() {
        // Создаем сервис с маленьким кэшем
        CacheService smallCache = new CacheService(logger) {
            @Override
            public int getMaxSize() {
                return 2;
            }
        };

        smallCache.put("key1", "value1", String.class);
        smallCache.put("key2", "value2", String.class);
        smallCache.put("key3", "value3", String.class);

        assertEquals(2, smallCache.size());
        assertFalse(smallCache.get("key1", String.class).isPresent());
    }
}