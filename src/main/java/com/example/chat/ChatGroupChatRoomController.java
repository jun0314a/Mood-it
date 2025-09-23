package com.example.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/chatroom")
public class ChatGroupChatRoomController {

    private final Map<String, ChatGroupChatRoom> rooms = new ConcurrentHashMap<>();

    @PostMapping("/create")
    public ResponseEntity<ChatGroupChatRoom> createRoom(@RequestParam String name) {
        String roomId = UUID.randomUUID().toString();
        ChatGroupChatRoom room = new ChatGroupChatRoom(roomId, name);
        rooms.put(roomId, room);
        return ResponseEntity.ok(room);
    }

    @GetMapping
    public ResponseEntity<List<ChatGroupChatRoom>> getRooms() {
        return ResponseEntity.ok(new ArrayList<>(rooms.values()));
    }
}
