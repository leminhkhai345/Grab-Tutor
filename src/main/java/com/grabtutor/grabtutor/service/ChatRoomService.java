package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.LoadChatRoomsResponse;
import com.grabtutor.grabtutor.dto.response.LoadMessagesResponse;
import com.grabtutor.grabtutor.dto.response.MessageResponse;

public interface ChatRoomService {
    MessageResponse saveMessage(MessageRequest request);
    LoadMessagesResponse loadMessages(String roomId);
    LoadChatRoomsResponse loadMyRooms();
    LoadChatRoomsResponse loadRooms(String userId);
    void submitSolution(String roomId);
    void confirmedSolution(String roomId);
    void inspectSolution(String roomId);
    void resolveSolution(String roomId, boolean isNormal);
}
