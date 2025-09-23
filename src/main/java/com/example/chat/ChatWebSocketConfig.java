package com.example.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class ChatWebSocketConfig implements WebSocketConfigurer {

    private final ChatController chatController;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatController, "/ws/chat/{roomId}")
                .setAllowedOrigins("*"); // 모든 도메인에서 접속 허용
    }
}
