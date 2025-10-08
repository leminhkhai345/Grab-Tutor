package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.MessageResponse;

public interface ChatService {
    MessageResponse saveMessage(MessageRequest request);

}
