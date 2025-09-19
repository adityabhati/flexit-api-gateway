package com.flexit.api_gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private RedisTemplate<String, String> redisTemplate;

    // Add token to list
    public void addTokenToList(String listKey, String token) {
        redisTemplate.opsForList().rightPush(listKey, token);
    }

    // Check if token exists using LPOS (Redis 6.0.6+)
    public boolean tokenExists(String listKey, String token) {
        Long position = redisTemplate.opsForList().indexOf(listKey, token);
        return position != null && position >= 0;
    }

}