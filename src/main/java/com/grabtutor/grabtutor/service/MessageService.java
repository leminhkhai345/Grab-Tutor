package com.grabtutor.grabtutor.service;

import com.grabtutor.grabtutor.dto.request.LoadMessagesRequest;
import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.dto.response.LoadMessagesResponse;
import com.grabtutor.grabtutor.dto.response.MessageResponse;

public interface MessageService {
    MessageResponse saveMessage(MessageRequest request);
    LoadMessagesResponse loadMessages(LoadMessagesRequest request);
}
