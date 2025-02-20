package org.scit.project.user.util;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier; // 🔥 추가
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    @Qualifier("redisTemplate")  // 🔥 필드에 직접 추가
    private final RedisTemplate<String, String> redisTemplate;

    public void setDataExpire(String email, String authCode, long expireTime) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email, authCode);
        redisTemplate.expire(email, expireTime, TimeUnit.SECONDS);
    }

    public boolean existData(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }
}
