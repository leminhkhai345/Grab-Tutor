package com.grabtutor.grabtutor.service.worker;


import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.entity.UserTransaction;
import com.grabtutor.grabtutor.enums.*;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.repository.AccountBalanceRepository;
import com.grabtutor.grabtutor.repository.ChatRoomRepository;
import com.grabtutor.grabtutor.repository.PostRepository;
import com.grabtutor.grabtutor.repository.UserTransactionRepository;
import com.grabtutor.grabtutor.websocket.NotificationService;
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

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    NotificationService notificationService;
    UserTransactionRepository userTransactionRepository;

    //Post quá hạn
    @Async
    @Scheduled(fixedDelay = 1000)
    public void checkPost() {
        long now = System.currentTimeMillis();
        Set<Object> jobs = redisTemplate.opsForZSet()
                .rangeByScore("post:expire", 0, now);

        if (jobs == null || jobs.isEmpty()) return;

        for (Object job : jobs) {
            try {
                var post = postRepository.findById(job.toString())
                        .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_EXIST));
                if (post.getTutorBids().isEmpty()) {
                    post.setDeleted(true);
                    postRepository.save(post);
                    notificationService.sendNotification(post.getUser().getId()
                            , "Removing Post"
                            , "Post " + post.getId() +" has been removed because no one offer to solve");
                }
            } catch (Exception e) {
                log.error(ErrorCode.POST_NOT_EXIST.getMessage(), e);
            } finally {
                redisTemplate.opsForZSet().remove("post:expire", job);
            }
        }
    }
    //Room timeout (không nộp bài trong thời gian quy định)
    @Async
    @Scheduled(fixedDelay = 1000)
    public void checkRoomTimeout() {
        long now = System.currentTimeMillis();
        Set<Object> jobs = redisTemplate.opsForZSet()
                .rangeByScore("chatroom:timeout", 0, now);

        if (jobs == null || jobs.isEmpty()) return;

        for (Object job : jobs) {
            try {
                var room =  chatRoomRepository.findById(job.toString())
                        .orElseThrow(()->new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
                if(room.getStatus().equals(RoomStatus.IN_PROGRESS)){

                    room.setStatus(RoomStatus.TIMEOUT);
                    room.getPost().setStatus(PostStatus.CLOSED);

                    postRepository.save(room.getPost());
                    notificationService.sendSignal(room.getId()
                        , MessageType.UPDATE
                        , "Submit timeout"
                        , "ChatRoom " + room.getId() +" has been closed because the tutor failed to submit the solution within the allowed time.");
                }

            } catch (Exception e) {
                log.error(ErrorCode.CHAT_ROOM_NOT_FOUND.getMessage(), e);
            } finally {
                redisTemplate.opsForZSet().remove("chatroom:timeout", job);
            }
        }
    }
    @Async
    @Scheduled(fixedDelay = 1000)
    public void checkRoomConfirmed() {
        long now = System.currentTimeMillis();
        Set<Object> jobs = redisTemplate.opsForZSet()
                .rangeByScore("chatroom:confirmed", 0, now);

        if (jobs == null || jobs.isEmpty()) return;

        for (Object job : jobs) {
            try {
                var room =  chatRoomRepository.findById(job.toString())
                        .orElseThrow(()->new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
                if(room.getStatus().equals(RoomStatus.SUBMITTED)){
                    room.setStatus(RoomStatus.CONFIRMED);
                    room.setChatEnabled(false);
                    room.getPost().setStatus(PostStatus.SOLVED);
                    postRepository.save(room.getPost());

                    var transaction = room.getPost().getUserTransaction();
                    var tutor = transaction.getReceiver();
                    transaction.setStatus(TransactionStatus.SUCCESS);
                    tutor.getAccountBalance().setBalance(tutor.getAccountBalance().getBalance() + transaction.getAmount());

                    accountBalanceRepository.save(tutor.getAccountBalance());
                    userTransactionRepository.save(transaction);

                    notificationService.sendSignal(room.getId()
                            , MessageType.UPDATE
                            , "Auto confirmed"
                            , "ChatRoom " + room.getId() +": Confirmed automatically because the time limit was exceeded");
                    notificationService.sendNotification(tutor.getId(),"Account balance", "+"+transaction.getAmount());

                }
            } catch (Exception e) {
                log.error(ErrorCode.CHAT_ROOM_NOT_FOUND.getMessage(), e);
            } finally {
                redisTemplate.opsForZSet().remove("chatroom:confirmed", job);
            }
        }
    }
}