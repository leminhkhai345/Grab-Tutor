package com.grabtutor.grabtutor.service.worker;


import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.enums.Role;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.repository.AccountBalanceRepository;
import com.grabtutor.grabtutor.repository.ChatRoomRepository;
import com.grabtutor.grabtutor.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableAsync
@EnableScheduling
public class ServiceWorker {

    RedisTemplate<String, Object> redisTemplate;
    PostRepository postRepository;
    ChatRoomRepository chatRoomRepository;
    AccountBalanceRepository  accountBalanceRepository;

    //Cái ni để xử lý post quá hạn chưa được accept
    @Async
    @Scheduled(fixedDelay = 1000)
    public void checkAccept() {
        long now = System.currentTimeMillis();
        Set<Object> jobs = redisTemplate.opsForZSet()
                .rangeByScore("post:expire", 0, now);

        if (jobs == null || jobs.isEmpty()) return;

        for (Object job : jobs) {
            try {
                var post = postRepository.findById(job.toString()).orElseThrow(()-> new AppException(ErrorCode.POST_NOT_EXIST));
                if (!post.isAccepted()){
                    post.setDeleted(true);
                    postRepository.save(post);
                    //Sẽ gọi notify tại đây
                }
            } catch (Exception e) {
                log.error(ErrorCode.POST_NOT_EXIST.getMessage(), e);
            } finally {
                redisTemplate.opsForZSet().remove("post:expire", job);
            }
        }
    }
    //Xử lý post được accept nhưng lại không được giải trong thời gian quy định
    //Gửi lại tiền giải bài cho học sinh
    @Async
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void checkSubmitted() {
        long now = System.currentTimeMillis();
        Set<Object> jobs = redisTemplate.opsForZSet()
                .rangeByScore("chatroom:submit", 0, now);

        if (jobs == null || jobs.isEmpty()) return;

        for (Object job : jobs) {
            try{
                var room = chatRoomRepository.findById(job.toString()).orElseThrow(() -> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
                if(!room.isSubmitted()){
                    for(User user : room.getUsers()){
                        if(user.getRole() == Role.USER){
                            var account = accountBalanceRepository.findByUserId(user.getId())
                                    .orElseThrow(()-> new AppException(ErrorCode.ACCOUNT_BALANCE_NOT_FOUND));
                            var post = room.getPost();
                            account.setBalance(account.getBalance() + post.getReward());
                            accountBalanceRepository.save(account);
                            post.setDeleted(true);
                            postRepository.save(post);
                        }
                    }
                }
            } catch (Exception e){
                log.error(e.getMessage() + " happened on checkSubmitted() :" + job.toString());
            }
            finally {
                redisTemplate.opsForZSet().remove("chatroom:submit", job);
            }
        }
    }

}