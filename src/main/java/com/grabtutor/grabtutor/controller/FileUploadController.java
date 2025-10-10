package com.grabtutor.grabtutor.controller;

import com.grabtutor.grabtutor.dto.response.ApiResponse;
import com.grabtutor.grabtutor.dto.response.FileUploadResponse;
import com.grabtutor.grabtutor.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FileUploadController {
    FileUploadService fileUploadService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> uploadFile(@Payload MultipartFile file) throws IOException {
        log.info("Uploading file {}", fileUploadService.uploadFile(file));
        return ApiResponse.builder()
                .data(FileUploadResponse.builder().fileUrl(fileUploadService.uploadFile(file)).build())
                .message("File uploaded successfully")
                .build();
    }
}
