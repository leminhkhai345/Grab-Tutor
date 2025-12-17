package com.grabtutor.grabtutor.service.worker;

import com.grabtutor.grabtutor.entity.Job;
import com.grabtutor.grabtutor.enums.*;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.socket.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobHandler {

    private final PostRepository postRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    private final NotificationService notificationService;
    private final UserTransactionRepository userTransactionRepository;

    @Transactional
    public void handle(Job job) {
        switch (job.getJobType()) {
            case POST_EXPIRE -> handlePostExpire(job.getRefId());
            case CHATROOM_TIMEOUT -> handleRoomTimeout(job.getRefId());
            case CHATROOM_CONFIRMED -> handleRoomConfirmed(job.getRefId());
        }
    }

    private void handlePostExpire(String postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXIST));

        if (post.isDeleted()) return;
        if (!post.getTutorBids().isEmpty()) return;

        post.setDeleted(true);
        postRepository.save(post);

        notificationService.sendNotification(
                post.getUser().getId(),
                "Removing Post",
                "Post " + post.getId() + " has been removed because no one offer to solve"
                , post.getId()
        );
    }

    private void handleRoomTimeout(String roomId) {
        var room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!room.getStatus().equals(RoomStatus.IN_PROGRESS)) return;

        room.setStatus(RoomStatus.TIMEOUT);
        room.getPost().setStatus(PostStatus.CLOSED);
        postRepository.save(room.getPost());

        notificationService.sendSignal(
                room.getId(),
                MessageType.TIMEOUT,
                "Submit timeout",
                "ChatRoom " + room.getId() + " has been closed because the tutor failed to submit the solution within the allowed time."
        );
    }

    private void handleRoomConfirmed(String roomId) {
        var room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!room.getStatus().equals(RoomStatus.SUBMITTED)) return;

        room.setStatus(RoomStatus.CONFIRMED);
        room.setChatEnabled(false);
        room.getPost().setStatus(PostStatus.SOLVED);
        postRepository.save(room.getPost());

        var transaction = room.getPost().getUserTransaction();
        if (transaction.getStatus() == TransactionStatus.SUCCESS) return; // tránh cộng tiền 2 lần

        var tutorBalance = transaction.getReceiver();
        transaction.setStatus(TransactionStatus.SUCCESS);
        tutorBalance.setBalance(tutorBalance.getBalance() + transaction.getAmount());

        accountBalanceRepository.save(tutorBalance);
        userTransactionRepository.save(transaction);

        notificationService.sendSignal(
                room.getId(),
                MessageType.UPDATE,
                "Auto confirmed",
                "ChatRoom " + room.getId() + ": Confirmed automatically because the time limit was exceeded"
        );
        notificationService.sendNotification(
                tutorBalance.getUser().getId(),
                "Account balance",
                "+" + transaction.getAmount(),
                tutorBalance.getId()
        );
    }
}
