package com.example.gooder.model;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MessageItem {
    private boolean isReaded;
    private String message;
    private String sender_id;
    private Timestamp time;

    public MessageItem(boolean isReaded, String message, String sender_id, Timestamp time) {
        this.isReaded = isReaded;
        this.message = message;
        this.sender_id = sender_id;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return sender_id;
    }

    public Timestamp getTime() {
        return time;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public String getFormattedTime() {
        // 顯示用：轉成字串
        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
        return sdf.format(time.toDate());
    }
}
