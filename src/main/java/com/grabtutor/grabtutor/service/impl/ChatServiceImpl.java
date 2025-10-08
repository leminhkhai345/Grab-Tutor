package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.MessageResponse;
import com.grabtutor.grabtutor.mapper.MessageMapper;
import com.grabtutor.grabtutor.repository.ChatRoomRepository;
import com.grabtutor.grabtutor.repository.MessageRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.ChatService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@Builder
@Data
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ChatServiceImpl implements ChatService {
    MessageMapper  messageMapper;
    UserRepository userRepository;
    ChatRoomRepository chatRoomRepository;
    MessageRepository messageRepository;
    @Override
    public MessageResponse saveMessage(MessageRequest request) {
        var message = messageMapper.ToMessage(request);
        message.setChatRoom(chatRoomRepository.getReferenceById(request.getRoomId()));
        message.setUser(userRepository.getReferenceById(request.getUserId()));
        messageRepository.save(message);
        return messageMapper.ToMessageResponse(message);
    }
}
