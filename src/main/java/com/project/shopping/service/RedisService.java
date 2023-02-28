package com.project.shopping.service;

import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.security.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private  final RedisTemplate<String, Object> redisTemplate;

    // refresh 토큰 생성 및 업데이트
    public void setValueWithExpire(String key, Object value, Duration ExpiresIn){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, ExpiresIn);
    }

    public void setValue(String key, Object value){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    // key를 이용한 refresh 토큰 읽기
    public Object getValue(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        Object value = valueOperations.get(key);
        if(value == null)
            return "null";

        return value;
    }

    // 로그아웃시 refresh  토큰 삭제
    public void deleteValue(String key){
        redisTemplate.delete(key);
    }
}
