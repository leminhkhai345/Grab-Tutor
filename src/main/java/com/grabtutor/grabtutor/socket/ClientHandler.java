package com.grabtutor.grabtutor.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grabtutor.grabtutor.configuration.CustomJwtDecoder;
import com.grabtutor.grabtutor.dto.request.MessageRequest;
import com.grabtutor.grabtutor.enums.MessageType;
import com.grabtutor.grabtutor.service.ChatRoomService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ClientHandler implements Runnable {

    // Các service được inject (phải là final)
    final Socket socket;
    final TcpSessionRegistry registry;
    final ObjectMapper objectMapper;
    final ChatRoomService chatRoomService;
    final CustomJwtDecoder jwtDecoder;

    // Các biến trạng thái của instance này
    InputStream in = null;
    OutputStream out = null;
    SocketWrapper wrapper = null;
    String authenticatedUserId = null; // Lưu userId sau khi xác thực

    // Constructor này được gọi bởi ApplicationContext.getBean(...)
    public ClientHandler(Socket socket,
                         TcpSessionRegistry registry,
                         ObjectMapper objectMapper,
                         ChatRoomService chatRoomService,
                         CustomJwtDecoder jwtDecoder) {
        this.socket = socket;
        this.registry = registry;
        this.objectMapper = objectMapper;
        this.chatRoomService = chatRoomService;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void run() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            // 1. Thực hiện bắt tay VÀ xác thực JWT
            String path = doHandshakeAndAuth();
            if (path == null) {
                // Handshake hoặc Auth thất bại, đóng kết nối
                log.warn("Handshake/Auth failed. Closing connection.");
                close();
                return;
            }

            // 2. Khởi tạo Wrapper và đăng ký vào Registry
            // (authenticatedUserId đã được set trong doHandshakeAndAuth)
            wrapper = new SocketWrapper(socket, in, out);
            wrapper.setPath(path);
            registry.register(wrapper, authenticatedUserId);

            log.info("Handshake success. User: {}, Path: {}", authenticatedUserId, path);

            // 3. Vòng lặp đọc tin nhắn
            while (socket.isConnected()) {
                String message = readFrame();
                if (message == null) break; // Client đóng kết nối

                // Xử lý tin nhắn
                handleChatMessage(message);
            }

        } catch (Exception e) {
            if (!(e instanceof EOFException)) {
                log.error("Connection error for user {}: {}", authenticatedUserId, e.getMessage());
            }
        } finally {
            if (wrapper != null) registry.remove(wrapper);
            else close();
        }
    }

    /**
     * Xử lý tin nhắn từ client (đã được xác thực)
     */
    private void handleChatMessage(String json) throws IOException {
        MessageRequest request = objectMapper.readValue(json, MessageRequest.class);

        // Xác thực đã được thực hiện ở handshake, không cần check lại

        switch (request.getType()) {
            case MessageType.JOIN:
                // Đăng ký đã được thực hiện ở hàm run(), ở đây chỉ join phòng
                registry.joinRoom(wrapper, request.getRoomId());
                break;
            case MessageType.MESSAGE:
                var response = chatRoomService.saveMessage(request);
                registry.sendMessageToRoom(request.getRoomId(), response);
                break;
            default:
                log.warn("Unknown message type from user {}: {}", authenticatedUserId, request.getType());
        }
    }

    /**
     * Thực hiện bắt tay WebSocket VÀ xác thực JWT từ query param
     * @return Path (nếu thành công) hoặc null (nếu thất bại)
     */
    private String doHandshakeAndAuth() throws Exception {
        Scanner s = new Scanner(in, StandardCharsets.UTF_8.name());
        String data = s.useDelimiter("\\r\\n\\r\\n").next();
        Matcher getMatcher = Pattern.compile("^GET (\\S+) HTTP").matcher(data);

        if (getMatcher.find()) {
            String fullPath = getMatcher.group(1); // Ví dụ: /ws/chat?token=xyz

            // --- BƯỚC 1: Tách Path và Query ---
            URI uri = URI.create(fullPath);
            String path = uri.getPath();
            Map<String, String> queryParams = parseQueryParams(uri.getQuery());

            // --- BƯỚC 2: Xác thực Token ---
            String token = queryParams.get("token");
            if (!authenticate(token)) {
                log.warn("Authentication failed for {}. Token missing or invalid.", socket.getRemoteSocketAddress());
                sendHttpResponse(401, "Unauthorized");
                return null; // Thất bại
            }

            // --- BƯỚC 3: Hoàn tất Handshake WebSocket ---
            Matcher keyMatcher = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
            if (keyMatcher.find()) {
                String clientKey = keyMatcher.group(1).trim();
                String acceptKey = Base64.getEncoder().encodeToString(
                        MessageDigest.getInstance("SHA-1").digest((clientKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes(StandardCharsets.UTF_8))
                );

                out.write(("HTTP/1.1 101 Switching Protocols\r\n" +
                        "Upgrade: websocket\r\n" +
                        "Connection: Upgrade\r\n" +
                        "Sec-WebSocket-Accept: " + acceptKey + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
                out.flush();
                return path; // Thành công, trả về path sạch
            }
        }

        // Yêu cầu không hợp lệ
        sendHttpResponse(400, "Bad Request");
        return null; // Thất bại
    }

    /**
     * Xác thực token và lưu userId
     */
    private boolean authenticate(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String userId = jwt.getClaimAsString("userId");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (userId != null) {
                this.authenticatedUserId = userId; // Lưu lại userId
                return true;
            }
        } catch (JwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Helper: Parse query string
     */
    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) return params;
        for (String param : query.split("&")) {
            try {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    params.put(URLDecoder.decode(entry[0], StandardCharsets.UTF_8),
                            URLDecoder.decode(entry[1], StandardCharsets.UTF_8));
                }
            } catch (Exception e) {
                log.warn("Failed to decode query param: {}", param, e);
            }
        }
        return params;
    }

    /**
     * Helper: Gửi phản hồi HTTP (cho trường hợp lỗi)
     */
    private void sendHttpResponse(int code, String reason) throws IOException {
        String response = "HTTP/1.1 " + code + " " + reason + "\r\n" +
                "Content-Length: 0\r\n" +
                "Connection: close\r\n\r\n";
        out.write(response.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    /**
     * Đọc và giải mã một WebSocket frame từ client
     */
    private String readFrame() throws IOException {
        int firstByte = in.read();
        if (firstByte == -1 || (firstByte & 0x0F) == 8) return null; // EOF or Close frame

        int secondByte = in.read();
        if (secondByte == -1) return null;
        boolean masked = (secondByte & 0x80) != 0;
        long len = secondByte & 0x7F;

        if (len == 126) len = ((in.read() & 0xFF) << 8) | (in.read() & 0xFF);
        else if (len == 127) for (int i = 0; i < 8; i++) in.read(); // Bỏ qua 8 bytes, không hỗ trợ tin siêu lớn

        byte[] key = new byte[4];
        if (masked && in.read(key, 0, 4) == -1) return null;

        byte[] payload = new byte[(int) len];
        int read = 0;
        while (read < len) {
            int r = in.read(payload, read, (int) len - read);
            if (r == -1) return null;
            read += r;
        }

        if (masked) {
            for (int i = 0; i < payload.length; i++) payload[i] = (byte) (payload[i] ^ key[i % 4]);
        }
        return new String(payload, StandardCharsets.UTF_8);
    }

    /**
     * Đóng socket an toàn
     */
    private void close() {
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException e) {}
    }
}