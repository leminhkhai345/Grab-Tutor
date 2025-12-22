package com.grabtutor.grabtutor.socket;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
@Getter
@Setter
public class SocketWrapper {
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    private String userId;
    private String roomId;
    private String path;

    public SocketWrapper(Socket socket, InputStream in, OutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    //Gửi về client
    public synchronized void send(String message) {
        if (socket.isClosed()) return;
        try {
            byte[] payload = message.getBytes(StandardCharsets.UTF_8);
            int len = payload.length;

            // Byte 1: FIN(1) + RSV(000) + Opcode(0001) = 10000001 = 0x81 = 129
            out.write(0x81);

            // Byte 2+: Payload length (Server to Client không mask)
            if (len <= 125) {
                out.write(len);
            } else if (len <= 65535) {
                out.write(126);
                out.write((len >> 8) & 0xFF);
                out.write(len & 0xFF);
            } else {
                // Hỗ trợ tin nhắn lớn hơn 65KB (64-bit length)
                out.write(127);
                for (int i = 7; i >= 0; i--) {
                    out.write((int) ((len >> (8 * i)) & 0xFF));
                }
            }

            out.write(payload);
            out.flush();
        } catch (IOException e) {
            log.error("Failed to send message to user {}", userId, e);
            close();
        }
    }

    public void close() {
        try {
            if (!socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }
}