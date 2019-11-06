package com.example.classinteraction.utils;

import java.util.Date;

public class ChatMessage {
    private String name;
    private String text;
    private String userid;
    private Date date;

    public ChatMessage() {
    }

    public ChatMessage(String userid, String name, String text, Date date) {
        this.userid = userid;
        this.name = name;
        this.text = text;
        this.date = date;

    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setUserid(String userid) { this.userid = userid; }

    public String getUserid() { return userid; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
