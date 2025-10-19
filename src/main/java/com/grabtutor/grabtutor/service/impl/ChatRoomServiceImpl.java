package com.grabtutor.grabtutor.service.impl;

import com.grabtutor.grabtutor.dto.request.LoadMessagesRequest;
import com.grabtutor.grabtutor.dto.response.LoadChatRoomsResponse;
import com.grabtutor.grabtutor.dto.response.LoadMessagesResponse;
import com.grabtutor.grabtutor.entity.User;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.mapper.ChatRoomMapper;
import com.grabtutor.grabtutor.mapper.MessageMapper;
import com.grabtutor.grabtutor.repository.ChatRoomRepository;
import com.grabtutor.grabtutor.repository.MessageRepository;
import com.grabtutor.grabtutor.repository.UserRepository;
import com.grabtutor.grabtutor.service.ChatRoomService;
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
public class ChatRoomServiceImpl extends ChatRoomService {
    MessageMapper messageMapper;
    ChatRoomMapper chatRoomMapper;
    UserRepository userRepository;
    ChatRoomRepository chatRoomRepository;
    MessageRepository messageRepository;

    @PreAuthorize("hasRole('USER') or hasRole('TUTOR')")
    @Override
    public LoadMessagesResponse loadMessages(LoadMessagesRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaim("userId");

        var room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(()-> new AppException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        boolean valid = false;
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

    @PreAuthorize("hasRole('USER') or hasRole('TUTOR')")
    @Override
    public LoadChatRoomsResponse loadRooms() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getClaim("userId");
        var user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        return LoadChatRoomsResponse.builder()
                .rooms(user.getChatRooms().stream().map(chatRoomMapper::toChatRoomResponse).toList())
                .build();
    }

    @Override
    public void submitSolution(SubmitSolutionRequest request) {

    }

    @Override
    public void confirmedSolution(ConfirmedSolution request) {

    }
}
