package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.LoadMessagesRequest;
import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.LoadChatRoomsResponse;
import com.grabtutor.grabtutor.dto.response.LoadMessagesResponse;
import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.enums.RoomStatus;
import com.grabtutor.grabtutor.enums.TransactionStatus;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ChatRoomMapper;
import com.grabtutor.grabtutor.mapper.MessageMapper;
import com.grabtutor.grabtutor.repository.*;
import com.grabtutor.grabtutor.service.ChatRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatRoomServiceImpl implements ChatRoomService {
    MessageMapper messageMapper;
    ChatRoomMapper chatRoomMapper;
    UserRepository userRepository;
    ChatRoomRepository chatRoomRepository;
    MessageRepository messageRepository;
    UserTransactionRepository userTransactionRepository;
    AccountBalanceRepository  accountBalanceRepository;

    @PreAuthorize("hasRole('USER') or hasRole('TUTOR')")
    @Override
    public MessageResponse saveMessage(MessageRequest request) {
        var message = messageMapper.ToMessage(request);
        message.setChatRoom(chatRoomRepository.findById(request.getRoomId()).orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND)));
        message.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        messageRepository.save(message);
        return messageMapper.ToMessageResponse(message);
    }
    @Override
    public LoadMessagesResponse loadMessages(LoadMessagesRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaim("userId");

        String role =  jwt.getClaim("role");

        var room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        boolean valid = false;

        if(role == "ADMIN"){
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

        var messages = messageRepository.findByChatRoomId(request.getRoomId());
        return LoadMessagesResponse.builder()
                .messages(messages.stream().map(messageMapper::ToMessageResponse).toList())
                .build();
    }

    @Override
    public LoadChatRoomsResponse loadMyRooms() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaimAsString("userId");
        var user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        return LoadChatRoomsResponse.builder()
                .rooms(user.getChatRooms().stream().map(chatRoomMapper::toChatRoomResponse).toList())
                .build();
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
        room.setStatus(RoomStatus.SUBMITTED);
        chatRoomRepository.save(room);
    }

    @PreAuthorize("hasRole('USER')")
    @Override
    @Transactional
    public void confirmedSolution(String roomId) {
        var room = chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        room.setStatus(RoomStatus.CONFIRMED);
        room.setChatEnabled(false);
        chatRoomRepository.save(room);

        var transaction = room.getPost().getUserTransaction();
        var tutor = transaction.getReceiver();
        transaction.setStatus(TransactionStatus.SUCCESS);
        tutor.getAccountBalance().setBalance(tutor.getAccountBalance().getBalance() + transaction.getAmount());
        accountBalanceRepository.save(tutor.getAccountBalance());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void inspectSolution(String roomId) {
        var room = chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        room.setStatus(RoomStatus.DISPUTED);
        room.setChatEnabled(false);
        chatRoomRepository.save(room);
        //ADMIN thực hiện kiểm tra tin nhắn
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void resolveSolution(String roomId, boolean isNormal) {
        var room = chatRoomRepository.findById(roomId)
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        RoomStatus status = isNormal ? RoomStatus.RESOLVED_NORMAL :  RoomStatus.RESOLVED_REFUND;

        room.setStatus(status);
        chatRoomRepository.save(room);

        var transaction = room.getPost().getUserTransaction();
        User receiver;
        if(isNormal){
            receiver = transaction.getReceiver();
        } else receiver = transaction.getSender();

        transaction.setStatus(TransactionStatus.FAILED);
        receiver.getAccountBalance().setBalance(receiver.getAccountBalance().getBalance() + transaction.getAmount());
        accountBalanceRepository.save(receiver.getAccountBalance());
    }
}
