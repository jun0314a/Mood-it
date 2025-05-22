package com.example.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatController extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomId(session);
        roomSessions.putIfAbsent(roomId, new CopyOnWriteArrayList<>());
        roomSessions.get(roomId).add(session);
    }

    @Override
protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);

    String roomId = getRoomId(session);
    List<WebSocketSession> sessions = roomSessions.get(roomId);

    if (sessions != null) {
        // 모든 접속 사용자 이름 수집
        Set<String> connectedUsers = new HashSet<>();
        for (WebSocketSession sess : sessions) {
            String user = getUserNameFromPayload(sess);
            if (user != null) {
                connectedUsers.add(user);
            }
        }

        // 읽은 사용자에 추가
        chatMessage.getReadUsers().addAll(connectedUsers);

        // 전송
        for (WebSocketSession sess : sessions) {
            if (sess.isOpen()) {
                sess.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            }
        }
    }
}

private String getUserNameFromPayload(WebSocketSession session) {
    
    String uri = session.getUri().toString();
    if (uri.contains("name=")) {
        return uri.substring(uri.indexOf("name=") + 5);
    }
    return null;
}


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        roomSessions.values().forEach(list -> list.remove(session));
    }

    private String getRoomId(WebSocketSession session) {
        String uri = session.getUri().toString();
        return uri.substring(uri.lastIndexOf('/') + 1);
    }

    private String getUserNameFromSession(WebSocketSession session) {
        String query = session.getUri().getQuery();
        if (query != null && query.startsWith("sender=")) {
            return query.substring("sender=".length());
        }
        return null;
    }
}
