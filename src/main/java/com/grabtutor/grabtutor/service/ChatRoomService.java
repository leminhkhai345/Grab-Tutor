package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.ChatRoomResponse;
import com.grabtutor.grabtutor.dto.response.LoadChatRoomsResponse;
import com.grabtutor.grabtutor.dto.response.LoadMessagesResponse;
import com.grabtutor.grabtutor.dto.response.MessageResponse;

public interface ChatRoomService {
    MessageResponse saveMessage(MessageRequest request);
    LoadMessagesResponse loadMessages(String roomId);
    LoadChatRoomsResponse loadMyRooms();
    ChatRoomResponse getChatRoom(String roomId);
    MessageResponse getMessage(String messageId);
    void deleteMessage(String messageId);
    MessageResponse updateMessage(String messageId, String content);
    LoadChatRoomsResponse loadRooms(String userId);
    void submitSolution(String roomId);
    void confirmedSolution(String roomId);
    void inspectSolution(String roomId);
    void resolveSolution(String roomId, boolean isNormal);
}
