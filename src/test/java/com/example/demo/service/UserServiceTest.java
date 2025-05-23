package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldReturnUserDto() {
        // Arrange
        User user = new User(1L, "testUser", "test@test.com", 25, "password123");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserDto result = userService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUserName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getAge(), result.getAge());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDtos() {
        // Arrange
        User user1 = new User(1L, "user1", "user1@test.com", 25, "password1");
        User user2 = new User(2L, "user2", "user2@test.com", 30, "password2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user1.getId(), result.get(0).getId());
        assertEquals(user2.getId(), result.get(1).getId());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUserDto() {
        // Arrange
        Long userId = 1L;
        User user = new User(userId, "testUser", "test@test.com", 25, "password123");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserDto result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturnNull() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        UserDto result = userService.getUserById(userId);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUserDto() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User(userId, "oldName", "old@test.com", 25, "oldPass");
        User updatedDetails = new User(userId, "newName", "new@test.com", 30, "newPass");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedDetails);

        // Act
        UserDto result = userService.updateUser(userId, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals(updatedDetails.getUserName(), result.getName());
        assertEquals(updatedDetails.getEmail(), result.getEmail());
        assertEquals(updatedDetails.getAge(), result.getAge());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldThrowException() {
        // Arrange
        Long userId = 1L;
        User updatedDetails = new User(userId, "newName", "new@test.com", 30, "newPass");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.updateUser(userId, updatedDetails));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldCallRepositoryDelete() {
        // Arrange
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }
}