package com.grabtutor.grabtutor.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grabtutor.grabtutor.configuration.CustomJwtDecoder;
import com.grabtutor.grabtutor.service.ChatRoomService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)

public class TcpServer {

    final int port = 8888;

    ServerSocket serverSocket;
    final ExecutorService clientPool = Executors.newCachedThreadPool();
    volatile boolean isRunning = true;

    final TcpSessionRegistry registry;
    final ObjectMapper objectMapper;
    final ChatRoomService chatRoomService;
    final CustomJwtDecoder jwtDecoder;

    @PostConstruct
    public void start() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                log.info("WebSocket Server started on port {}", port);

                while (isRunning) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(clientSocket, registry, objectMapper, chatRoomService, jwtDecoder);
                    clientPool.submit(handler);
                }
            } catch (IOException e) {
                if (isRunning) log.error("Server crashed", e);
            }
        }).start();
    }

    @PreDestroy
    public void stop() {
        isRunning = false;
        try { if (serverSocket != null) serverSocket.close(); } catch (IOException ignored) {}
        clientPool.shutdownNow();
    }
}