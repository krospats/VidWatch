package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserAndVideoRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserAndVideoService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserAndVideoRepository userRepository;
    private final CacheService cacheService;

    @Autowired
    public UserAndVideoService(UserAndVideoRepository userRepository, CacheService cacheService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }



    public List<UserDto> getUsersByVideoName(String videoName) {
        String cacheKey = "users_by_video_" + videoName;
        logger.debug("Checking cache for users with video");

        // Получаем список UserDto из кэша
        @SuppressWarnings("unchecked")
        Optional<List<UserDto>> cachedUserDtos = cacheService.get(cacheKey, List.class)
                .map(list -> (List<UserDto>) list);


        if (cachedUserDtos.isPresent()) {
            logger.info("Returning users with video from cache");
            return cachedUserDtos.get();

        }
        logger.debug("Users with video not found in cache, querying database");
        List<User> users = userRepository.findUsersByVideoName(videoName);
        List<UserDto> userDtos = users.stream()
                .map(UserDto::new).toList();

        if (!userDtos.isEmpty()) {
            logger.debug("Caching users with video");

            // Конвертируем в List<UserDto> для кэширования
            cacheService.put(cacheKey, userDtos, List.class);
        }



        return userDtos;

    }

}
