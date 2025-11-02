package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.ChatRoomResponse;
import com.grabtutor.grabtutor.dto.response.LoadChatRoomsResponse;
import com.grabtutor.grabtutor.dto.response.LoadMessagesResponse;
import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.entity.ChatRoom;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.enums.MessageType;
import com.grabtutor.grabtutor.enums.PostStatus;
import com.grabtutor.grabtutor.enums.RoomStatus;
import com.grabtutor.grabtutor.enums.TransactionStatus;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ChatRoomMapper;
import com.grabtutor.grabtutor.mapper.MessageMapper;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.ChatRoomService;
import com.grabtutor.grabtutor.websocket.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {
    MessageMapper messageMapper;
    ChatRoomMapper chatRoomMapper;
    UserRepository userRepository;
    ChatRoomRepository chatRoomRepository;
    PostRepository postRepository;
    MessageRepository messageRepository;
    UserTransactionRepository userTransactionRepository;
    AccountBalanceRepository  accountBalanceRepository;
    NotificationService notificationService;

    @Override
    public MessageResponse saveMessage(MessageRequest request) {
        var message = messageMapper.ToMessage(request);
        message.setChatRoom(chatRoomRepository.findById(request.getRoomId()).orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND)));
        message.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        messageRepository.save(message);
        return messageMapper.ToMessageResponse(message);
    }
    @Override
    public LoadMessagesResponse loadMessages(String roomId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaim("userId");

        String role =  jwt.getClaim("scope");

        var room = chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        boolean valid = false;

        if(Objects.equals(role, "ROLE_ADMIN")){
            valid = true;
        }
        for(User user : room.getUsers() ){
            if(user.getId().equals(userId)){
                valid = true;
                break;
            }
        }
        if(!valid){
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        var messages = messageRepository.findByChatRoomId(roomId);
        return LoadMessagesResponse.builder()
                .messages(messages.stream().map(messageMapper::ToMessageResponse).toList())
                .build();
    }

    @Override
    public LoadChatRoomsResponse loadMyRooms() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return LoadChatRoomsResponse.builder()
                .rooms(user.getChatRooms().stream().map(chatRoomMapper::toChatRoomResponse).toList())
                .build();
    }

    @Override
    public ChatRoomResponse getChatRoom(String roomId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        String role =  jwt.getClaim("scope");

        var room = chatRoomRepository.findById(roomId).orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        boolean valid = room.getUsers()
                .stream()
                .anyMatch(user -> user.getId().equals(userId));
        if(Objects.equals(role, "ROLE_ADMIN")) valid = true;
        if(!valid){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        return chatRoomMapper.toChatRoomResponse(room);
    }

    @Override
    public MessageResponse getMessage(String messageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        String role =  jwt.getClaim("scope");

        var message = messageRepository.findById(messageId).orElseThrow(()->new AppException(ErrorCode.MESSAGE_NOT_FOUND));
        boolean valid = message.getChatRoom()
                .getUsers()
                .stream()
                .anyMatch(user -> user.getId().equals(userId));
        if(Objects.equals(role, "ROLE_ADMIN")) valid = true;
        if(!valid){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        return messageMapper.ToMessageResponse(message);
    }

    @Override
    public void deleteMessage(String messageId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");

        var message = messageRepository.findById(messageId).orElseThrow(()->new AppException(ErrorCode.MESSAGE_NOT_FOUND));
        if(!message.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        message.setDeleted(true);
        messageRepository.save(message);
    }

    @Override
    public MessageResponse updateMessage(String messageId, String content) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");

        var message = messageRepository.findById(messageId).orElseThrow(()->new AppException(ErrorCode.MESSAGE_NOT_FOUND));
        if(!message.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        message.setMessage(content);
        messageRepository.save(message);
        return messageMapper.ToMessageResponse(message);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public LoadChatRoomsResponse loadRooms(String userId) {
        var user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        return LoadChatRoomsResponse.builder()
                .rooms(user.getChatRooms().stream().map(chatRoomMapper::toChatRoomResponse).toList())
                .build();
    }

    @PreAuthorize("hasRole('TUTOR')")
    @Override
    public void submitSolution(String roomId) {
        var room = chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        if(!room.getStatus().equals(RoomStatus.IN_PROGRESS)) throw new AppException(ErrorCode.FORBIDDEN);
        room.setStatus(RoomStatus.SUBMITTED);
        chatRoomRepository.save(room);
        notificationService.sendSignal(roomId, MessageType.SUBMIT, "Solution submitted", "");
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    @Transactional
    public void confirmedSolution(String roomId) {
        var post = postRepository.findByChatRoomId(roomId).orElseThrow(()-> new AppException(ErrorCode.POST_NOT_EXIST));
        var room = post.getChatRoom();

        if(!room.getStatus().equals(RoomStatus.SUBMITTED)) throw new AppException(ErrorCode.FORBIDDEN);
        room.setStatus(RoomStatus.CONFIRMED);
        room.setChatEnabled(false);
        post.setStatus(PostStatus.SOLVED);
        postRepository.save(post);

        var transaction = room.getPost().getUserTransaction();
        var tutor = transaction.getReceiver();
        transaction.setStatus(TransactionStatus.SUCCESS);
        tutor.getAccountBalance().setBalance(tutor.getAccountBalance().getBalance() + transaction.getAmount());

        accountBalanceRepository.save(tutor.getAccountBalance());
        userTransactionRepository.save(transaction);
        notificationService.sendSignal(roomId, MessageType.CONFIRM, "Solution confirmed", "");
        notificationService.sendNotification(tutor.getId(),"Account balance", "+"+transaction.getAmount());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void inspectSolution(String roomId) {
        var room = chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        if(!room.getStatus().equals(RoomStatus.SUBMITTED)) throw new AppException(ErrorCode.FORBIDDEN);
        room.setStatus(RoomStatus.DISPUTED);
        room.setChatEnabled(false);
        chatRoomRepository.save(room);
        //ADMIN thực hiện kiểm tra tin nhắn

        notificationService.sendSignal(roomId, MessageType.DISPUTE
                , "ChatRoom is in dispute"
                , "Admin is checking the solution, please wait.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void resolveSolution(String roomId, boolean isNormal) {
        var post = postRepository.findByChatRoomId(roomId).orElseThrow(()-> new AppException(ErrorCode.POST_NOT_EXIST));
        var room = post.getChatRoom();

        if(!room.getStatus().equals(RoomStatus.DISPUTED)) throw new AppException(ErrorCode.CHAT_ROOM_NOT_IN_DISPUTED);
        RoomStatus status = isNormal ? RoomStatus.RESOLVED_NORMAL :  RoomStatus.RESOLVED_REFUND;
        room.setStatus(status);
        room.setChatEnabled(false);
        postRepository.save(post);

        var transaction = room.getPost().getUserTransaction();
        User receiver;
        if(isNormal){
            receiver = transaction.getReceiver();
            transaction.setStatus(TransactionStatus.SUCCESS);
        } else{
            receiver = transaction.getSender();
            transaction.setStatus(TransactionStatus.FAILED);
        }
        receiver.getAccountBalance().setBalance(receiver.getAccountBalance().getBalance() + transaction.getAmount());

        accountBalanceRepository.save(receiver.getAccountBalance());
        userTransactionRepository.save(transaction);

        notificationService.sendSignal(roomId, MessageType.RESOLVE
                , "ChatRoom resolved"
                , "The report has been reviewed. "+receiver.getEmail() +" were found to be in the right.");
        notificationService.sendNotification(receiver.getId(),"Account balance", "+"+transaction.getAmount());

    }
}
