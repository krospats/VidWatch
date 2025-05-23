package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserAndVideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAndVideoServiceTest {

    @Mock
    private UserAndVideoRepository userRepository;

    @Mock
    private CacheService cacheService;

    private UserAndVideoService userAndVideoService;

    private final String testVideoName = "testVideo";
    private final String cacheKey = "users_by_video_" + testVideoName;

    @BeforeEach
    void setUp() {
        // Создаем реальный экземпляр сервиса
        userAndVideoService = new UserAndVideoService(userRepository, cacheService);

        // Подменяем логгер в сервисе
        Logger mockLogger = mock(Logger.class);
        setField(userAndVideoService, "logger", mockLogger);
    }

    @Test
    void getUsersByVideoName_ShouldReturnFromCacheWhenPresent() {
        // Arrange
        UserDto userDto1 = createTestUserDto(1L);
        UserDto userDto2 = createTestUserDto(2L);
        List<UserDto> cachedUsers = Arrays.asList(userDto1, userDto2);

        when(cacheService.get(cacheKey, List.class))
                .thenReturn(Optional.of(cachedUsers));

        // Act
        List<UserDto> result = userAndVideoService.getUsersByVideoName(testVideoName);

        // Assert
        assertEquals(2, result.size());
        assertEquals(userDto1.getId(), result.get(0).getId());
        assertEquals(userDto2.getId(), result.get(1).getId());

        verify(getLogger()).debug("Checking cache for users with video");
        verify(getLogger()).info("Returning users with video from cache");
        verify(cacheService, never()).put(any(), any(), any());
        verify(userRepository, never()).findUsersByVideoName(any());
    }

    @Test
    void getUsersByVideoName_ShouldQueryDBAndCacheWhenNotInCache() {
        // Arrange
        User user1 = createTestUser(1L);
        User user2 = createTestUser(2L);
        List<User> dbUsers = Arrays.asList(user1, user2);

        when(cacheService.get(cacheKey, List.class))
                .thenReturn(Optional.empty());
        when(userRepository.findUsersByVideoName(testVideoName))
                .thenReturn(dbUsers);

        // Act
        List<UserDto> result = userAndVideoService.getUsersByVideoName(testVideoName);

        // Assert
        assertEquals(2, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());

        verify(getLogger()).debug("Checking cache for users with video");
        verify(getLogger()).debug("Users with video not found in cache, querying database");
        verify(getLogger()).debug("Caching users with video");

        ArgumentCaptor<List<UserDto>> cacheCaptor = ArgumentCaptor.forClass(List.class);
        verify(cacheService).put(eq(cacheKey), cacheCaptor.capture(), eq(List.class));

        List<UserDto> cachedValue = cacheCaptor.getValue();
        assertEquals(2, cachedValue.size());
        assertEquals(user1.getId(), cachedValue.get(0).getId());
        assertEquals(user2.getId(), cachedValue.get(1).getId());
    }

    @Test
    void getUsersByVideoName_ShouldHandleEmptyResultFromDB() {
        // Arrange
        when(cacheService.get(cacheKey, List.class))
                .thenReturn(Optional.empty());
        when(userRepository.findUsersByVideoName(testVideoName))
                .thenReturn(Collections.emptyList());

        // Act
        List<UserDto> result = userAndVideoService.getUsersByVideoName(testVideoName);

        // Assert
        assertTrue(result.isEmpty());
        verify(getLogger()).debug("Checking cache for users with video");
        verify(getLogger()).debug("Users with video not found in cache, querying database");
        verify(cacheService, never()).put(any(), any(), any());
    }

    private User createTestUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setUserName("user" + id);
        user.setEmail("user" + id + "@test.com");
        user.setAge(20 + id.intValue());
        user.setPassword("password" + id);
        return user;
    }

    private UserDto createTestUserDto(Long id) {
        return new UserDto(createTestUser(id));
    }

    private Logger getLogger() {
        try {
            java.lang.reflect.Field field = UserAndVideoService.class.getDeclaredField("logger");
            field.setAccessible(true);
            return (Logger) field.get(userAndVideoService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get logger from service", e);
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field " + fieldName, e);
        }
    }
}