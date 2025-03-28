package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserAndVideoRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserAndVideoService {
    private static final Logger logger = LoggerFactory.getLogger(UserAndVideoService.class);
    private final UserAndVideoRepository userRepository;
    private final CacheService cacheService;

    @Autowired
    public UserAndVideoService(UserAndVideoRepository userRepository, CacheService cacheService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }

    public Optional<User> getUserWithVideos(Long id) {
        String cacheKey = "user_" + id;
        logger.debug("Checking cache for user: {}", id);

        // Получаем UserDto из кэша
        Optional<UserDto> cachedUserDto = cacheService.get(cacheKey, UserDto.class);

        if (cachedUserDto.isPresent()) {
            logger.info("Returning user {} from cache", id);
            // Конвертируем UserDto обратно в User
            return Optional.of(convertToUser(cachedUserDto.get()));
        }

        logger.debug("User {} not found in cache, querying database", id);
        Optional<User> user = userRepository.findByIdWithVideos(id);

        if (user.isPresent()) {
            logger.debug("Caching user {}", id);
            // Сохраняем UserDto в кэш
            cacheService.put(cacheKey, new UserDto(user.get()), UserDto.class);
        }

        return user;
    }

    public List<UserDto> getUsersByVideoName(String videoName) {
        String cacheKey = "users_by_video_" + videoName;
        logger.debug("Checking cache for users with video: {}", videoName);

        // Получаем список UserDto из кэша
        Optional<List<UserDto>> cachedUserDtos = cacheService.get(cacheKey, List.class)
                .map(list -> (List<UserDto>) list);

        if (cachedUserDtos.isPresent()) {
            logger.info("Returning users with video {} from cache", videoName);
            return cachedUserDtos.get();

        }
        logger.debug("Users with video {} not found in cache, querying database", videoName);
        List<User> users = userRepository.findUsersByVideoName(videoName);
        List<UserDto> userDtos = users.stream()
                .map(UserDto::new)
                .collect(Collectors.toList());

        if (!userDtos.isEmpty()) {
            logger.debug("Caching {} users with video {}", users.size(), videoName);

            // Конвертируем в List<UserDto> для кэширования
            cacheService.put(cacheKey, userDtos, List.class);
        }



        return userDtos;

    }

    private User convertToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUserName(userDto.getName());
        user.setEmail(userDto.getEmail());
        // Остальные поля при необходимости
        return user;
    }
}