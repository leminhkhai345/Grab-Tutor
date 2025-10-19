package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.LoadMessagesRequest;
import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.LoadChatRoomsResponse;
import com.grabtutor.grabtutor.dto.response.LoadMessagesResponse;
import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ChatRoomMapper;
import com.grabtutor.grabtutor.mapper.MessageMapper;
import com.grabtutor.grabtutor.repository.ChatRoomRepository;
import com.grabtutor.grabtutor.repository.MessageRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.ChatService;
import lombok.Builder;
import lombok.Data;
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
public class ChatServiceImpl implements ChatService {
    MessageMapper  messageMapper;
    ChatRoomMapper chatRoomMapper;
    UserRepository userRepository;
    ChatRoomRepository chatRoomRepository;
    MessageRepository messageRepository;

    @PreAuthorize("hasRole('USER') or hasRole('TUTOR')")
    @Override
    public MessageResponse saveMessage(MessageRequest request) {
        var message = messageMapper.ToMessage(request);
        message.setChatRoom(chatRoomRepository.findById(request.getRoomId()).orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND)));
        message.setUser(userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        messageRepository.save(message);
        return messageMapper.ToMessageResponse(message);
    }

}

