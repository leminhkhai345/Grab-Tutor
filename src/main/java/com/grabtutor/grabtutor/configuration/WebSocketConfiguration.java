package com.grabtutor.grabtutor.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // endpoint mà FE sẽ connect tới: ws://localhost:8080/ws
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // fallback nếu trình duyệt không hỗ trợ ws
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // các prefix để subscribe và gửi message
        registry.enableSimpleBroker("/topic", "/queue"); // topic = chat channel, queue = notification riêng
        registry.setApplicationDestinationPrefixes("/app");
    }
}
