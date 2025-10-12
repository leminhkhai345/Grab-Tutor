package com.grabtutor.grabtutor.service.worker;


import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostWorker {

//    RedisTemplate<String, Object> redisTemplate;
//    PostRepository postRepository;
//
//    //Cái ni để xử lý post quá hạn chưa được accept
//    @Scheduled(fixedRate = 2000)
//    public void processPost() {
//        long now = System.currentTimeMillis();
//        Set<Object> jobs = redisTemplate.opsForZSet()
//                .rangeByScore("post:expire", 0, now);
//
//        if (jobs == null || jobs.isEmpty()) return;
//
//        for (Object job : jobs) {
//            try {
//                var post = postRepository.findById(job.toString()).orElseThrow(()-> new AppException(ErrorCode.POST_NOT_EXIST));
//                if (!post.isAccepted()){
//                    post.setDeleted(true);
//                    postRepository.save(post);
//                }
//            } catch (Exception e) {
//                log.error(ErrorCode.POST_NOT_EXIST.getMessage(), e);
//            } finally {
//                redisTemplate.opsForZSet().remove("post:expire", job);
//            }
//        }
//    }
//    //Xử lý post được accept nhưng lại không được giải trong thời gian quy định
//    @Scheduled(fixedRate = 2000)
//    public void processPostTimeout() {
//        long now = System.currentTimeMillis();
//        Set<Object> jobs = redisTemplate.opsForZSet()
//                .rangeByScore("post:timeout", 0, now);
//
//        if (jobs == null || jobs.isEmpty()) return;
//
//        for (Object job : jobs) {
//            try {
//                var post = postRepository.findById(job.toString()).orElseThrow(()-> new AppException(ErrorCode.POST_NOT_EXIST));
//                post.setDeleted(true);
//                postRepository.save(post);
//            } catch (Exception e) {
//                log.error(ErrorCode.POST_NOT_EXIST.getMessage(), e);
//            } finally {
//                redisTemplate.opsForZSet().remove("post:expire", job);
//            }
//        }
//    }

}