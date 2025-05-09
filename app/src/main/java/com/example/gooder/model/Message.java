package com.example.gooder.model;

public class Message {
    private String message;
    private String sender_id;
    private String time;
    private boolean isReaded;

    public Message() {

    }

    public Message(String message, String sender_id, String time, boolean isReaded) {
        this.message = message;
        this.sender_id = sender_id;
        this.time = time;
        this.isReaded = isReaded;
    }

    public String getMessage() {
        return message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public String getTime() {
        return time;
    }

    public boolean isReaded() {
        return isReaded;
    }
}
