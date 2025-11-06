package com.grabtutor.grabtutor.service.impl;

import com.cloudinary.Cloudinary;
import com.grabtutor.grabtutor.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        if (isVideoFile(multipartFile)) {
            log.info("Routing to uploadVideo for: {}", multipartFile.getOriginalFilename());
            return uploadVideo(multipartFile);
        } else {
            log.info("Routing to uploadImageOrRaw for: {}", multipartFile.getOriginalFilename());
            return uploadImageOrRaw(multipartFile);
        }
    }

    private boolean isVideoFile(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        if (contentType != null && contentType.startsWith("video")) {
            return true;
        }
        String filename = multipartFile.getOriginalFilename();
        if (filename != null) {
            String lowerCaseFilename = filename.toLowerCase();
            return lowerCaseFilename.endsWith(".mp4")
                    || lowerCaseFilename.endsWith(".mov")
                    || lowerCaseFilename.endsWith(".avi")
                    || lowerCaseFilename.endsWith(".mkv")
                    || lowerCaseFilename.endsWith(".webm");
        }
        return false;
    }

    private String uploadVideo(MultipartFile multipartFile) throws IOException {
        File tempFile = null;
        try {
            Path tempPath = Files.createTempFile("upload_temp_video_", multipartFile.getOriginalFilename());
            tempFile = tempPath.toFile();
            multipartFile.transferTo(tempFile);

            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("resource_type", "video");
            uploadParams.put("chunk_size", 6_000_000);
            uploadParams.put("public_id", UUID.randomUUID().toString());
            uploadParams.put("streaming_profile", "auto");

            log.info("Attempting large video upload to Cloudinary for: {}", multipartFile.getOriginalFilename());
            Map uploadResult = cloudinary.uploader().uploadLarge(tempFile, uploadParams);
            log.info("Cloudinary upload successful.");

            List<Map> eagerResults = (List<Map>) uploadResult.get("eager");
            if (eagerResults != null && !eagerResults.isEmpty()) {
                for (Map eagerMap : eagerResults) {
                    if ("m3u8".equals(eagerMap.get("format"))) {
                        return eagerMap.get("secure_url").toString();
                    }
                }
            }
            log.warn("M3U8 URL not found, returning original video URL");
            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            log.error("Cloudinary video upload failed for: {}", multipartFile.getOriginalFilename(), e);
            throw new IOException("Failed to upload video to Cloudinary. Error: " + e.getMessage(), e);
        } finally {
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }

    private String uploadImageOrRaw(MultipartFile multipartFile) throws IOException {
        try {
            log.info("Attempting image/raw upload to Cloudinary for: {}", multipartFile.getOriginalFilename());


            Map<String, Object> uploadParams = new HashMap<>();
            uploadParams.put("resource_type", "auto");
            uploadParams.put("public_id", UUID.randomUUID().toString());

            return cloudinary.uploader()
                    .upload(
                            multipartFile.getBytes(),
                            uploadParams // Truyền HashMap vào
                    )
                    .get("secure_url")
                    .toString();

        } catch (Exception e) {
            log.error("Cloudinary image/raw upload failed for: {}", multipartFile.getOriginalFilename(), e);
            throw new IOException("Failed to upload image/raw file to Cloudinary. Error: " + e.getMessage(), e);
        }
    }
}