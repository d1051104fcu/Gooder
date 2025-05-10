package com.example.gooder.model;

public class ChatItem {

    private String chatId;
    private String name;
    private String lastMessage;
    private String time;
    private int avatarResId;
    private int unreadCount;

    public ChatItem(String chatId, String name, String lastMessage, String time, int avatarResId, int unreadCount) {
        this.chatId = chatId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.avatarResId = avatarResId;
        this.unreadCount = unreadCount;
    }

    public String getChatId(){
        return chatId;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}
