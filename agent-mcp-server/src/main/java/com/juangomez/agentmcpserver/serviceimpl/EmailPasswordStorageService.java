package com.juangomez.agentmcpserver.serviceimpl;

import com.juangomez.agentmcpserver.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailPasswordStorageService implements StorageService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void saveValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public boolean isKeySaved(String key) {
        // If the token is in db
        return redisTemplate.opsForValue().get(key) != null;
    }

    // Gets the password by username
    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
