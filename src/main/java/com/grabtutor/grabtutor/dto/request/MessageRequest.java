package com.grabtutor.grabtutor.dto.request;

import com.grabtutor.grabtutor.enums.MessageType;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MessageRequest {
    String userId;
    String roomId;
    MessageType type;
    String message;
    String fileName;
    String fileUrl;
}
