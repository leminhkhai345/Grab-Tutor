package com.grabtutor.grabtutor.dto.request;

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
    String message;
    String fileName;
    String fileUrl;
}
