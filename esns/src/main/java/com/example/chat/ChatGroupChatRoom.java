package com.example.chat;

public class ChatGroupChatRoom {
    private String roomId;
    private String name;

    public ChatGroupChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    // Getters & Setters
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
