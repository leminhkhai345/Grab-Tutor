//package com.grabtutor.grabtutor.websocket;
//
//import com.grabtutor.grabtutor.configuration.CustomJwtDecoder;
//import jakarta.websocket.HandshakeResponse;
//import jakarta.websocket.server.HandshakeRequest;
//import jakarta.websocket.server.ServerEndpointConfig;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Map;
//
//@Component
//@Slf4j
//@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
//
//public class AuthHandshakeConfigurator extends ServerEndpointConfig.Configurator {
//
//    // Tiêm JwtDecoder (đã được Spring Security OAuth2 tự cấu hình)
//    static CustomJwtDecoder jwtDecoder;
//
//    @Autowired
//    public void setJwtDecoder(CustomJwtDecoder decoder) {
//        AuthHandshakeConfigurator.jwtDecoder = decoder;
//    }
//
//    @Override
//    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//        super.modifyHandshake(sec, request, response);
//
//        Map<String, List<String>> params = request.getParameterMap();
//        List<String> tokens = params.get("token");
//
//        if (tokens != null && !tokens.isEmpty()) {
//            String tokenValue = tokens.get(0);
//            try {
//                Jwt jwt = jwtDecoder.decode(tokenValue);
//
//                String userId = jwt.getClaimAsString("userId");
//
//                if (userId != null) {
//                    // Lưu userId đã xác thực vào session properties
//                    sec.getUserProperties().put("userId", userId);
//                    log.info("Handshake successful, user: {}", userId);
//                    return;
//                }
//            } catch (JwtException e) {
//                log.warn("Handshake failed: Invalid JWT", e);
//            }
//        }
//
//        log.warn("Handshake failed: Missing or invalid token");
//        // Không lưu "userId", @OnOpen sẽ tự động từ chối kết nối
//    }
//}