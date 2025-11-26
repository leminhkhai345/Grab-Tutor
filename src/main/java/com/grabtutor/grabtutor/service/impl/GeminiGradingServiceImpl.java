package com.grabtutor.grabtutor.service.impl;


import com.grabtutor.grabtutor.entity.Post;
import com.grabtutor.grabtutor.exception.AppException;
import com.grabtutor.grabtutor.exception.ErrorCode;
import com.grabtutor.grabtutor.repository.PostRepository;
import com.grabtutor.grabtutor.service.GeminiGradingService;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.data.message.AiMessage;
import lombok.AccessLevel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.client.RestClient;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeminiGradingServiceImpl implements GeminiGradingService {
    final ChatLanguageModel chatModel;
    RestClient restClient = RestClient.create();
    final PostRepository postRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public String checkHomework(String postId, MultipartFile answerFile) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
            String imageUrl = post.getImageUrl();
            // 2. Tải ảnh đề bài từ URL
            byte[] imageBytes = restClient.get().uri(imageUrl).retrieve().body(byte[].class);

            // 3. Đọc text bài làm
            String studentAnswer = new String(answerFile.getBytes(), StandardCharsets.UTF_8);
            String promptText = "Đây là đề bài (ảnh) và bài làm: " + studentAnswer
                    + ". Hãy kiểm tra đúng sai. Nếu sai hãy giải thích lỗi sai và đưa ra đáp án đúng.";

            // 4. GỌI GEMINI QUA LANGCHAIN4J (Phần quan trọng nhất)
            // Tạo message chứa cả Text và Ảnh
            UserMessage userMessage = UserMessage.from(
                    TextContent.from(promptText),
                    ImageContent.from(Base64.getEncoder().encodeToString(imageBytes), "image/jpeg")
            );

            // Gửi đi và nhận kết quả
            Response<AiMessage> response = chatModel.generate(userMessage);

            // Trả về text câu trả lời
            return response.content().text();

        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}