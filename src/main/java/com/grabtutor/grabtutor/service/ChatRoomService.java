package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.LoadMessagesRequest;
import com.grabtutor.grabtutor.dto.response.LoadChatRoomsResponse;
import com.grabtutor.grabtutor.dto.response.LoadMessagesResponse;

public interface ChatRoomService {
    LoadMessagesResponse loadMessages(LoadMessagesRequest request);
    LoadChatRoomsResponse loadRooms();
    void submitSolution(SubmitSolutionRequest request);
    void confirmedSolution(ConfirmedSolution request);
}
