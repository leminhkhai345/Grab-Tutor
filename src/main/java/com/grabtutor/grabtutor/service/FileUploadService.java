package com.grabtutor.grabtutor.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

public interface FileUploadService {
    String uploadFile(MultipartFile multipartFile) throws IOException;

}