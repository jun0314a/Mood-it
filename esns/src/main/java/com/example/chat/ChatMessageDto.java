package com.example.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
    private String type;
    private String roomId;
    private String sender;
    private String message;
    private Set<String> readUsers = new HashSet<>(); // 중복 방지

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Set<String> getReadUsers() { return readUsers; }
    public void setReadUsers(Set<String> readUsers) { this.readUsers = readUsers; }
}
