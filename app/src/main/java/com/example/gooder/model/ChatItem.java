package com.example.gooder.model;

import com.google.firebase.Timestamp;

public class ChatItem {

    private String chatId;
    private String anotherChaterId;
    private String name;
    private String lastMessage;
    private Timestamp time;
    private int avatarResId;
    private int unreadCount;

    public ChatItem(String chatId, String anotherChaterId, String name, String lastMessage, Timestamp time, int avatarResId, int unreadCount) {
        this.chatId = chatId;
        this.anotherChaterId = anotherChaterId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.avatarResId = avatarResId;
        this.unreadCount = unreadCount;
    }

    public String getChatId(){
        return chatId;
    }

    public String getAnotherChaterId(){return anotherChaterId;}

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time){
        this.time = time;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}
