package com.grabtutor.grabtutor.service.worker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceJob {

    RedisTemplate<String, Object> redisTemplate;
    public void addCheckPost(String postId, LocalDateTime createdTime){
        redisTemplate.opsForZSet().add("post:expire", postId,
                createdTime
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli() + 60000*30);
    }
    public void addCheckRoomTimeout(String roomId, LocalDateTime createdTime){
        redisTemplate.opsForZSet().add("chatroom:timeout", roomId,
                createdTime
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli() + 60000*15);
    }
    public void addCheckRoomConfirmed(String roomId, LocalDateTime createdTime){
        redisTemplate.opsForZSet().add("chatroom:confirmed", roomId,
                createdTime
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli() + 60000*15);
    }


}
