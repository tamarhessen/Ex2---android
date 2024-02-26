package com.example.login.facebookdesign;

public class Comment {
    private int id;
    private String content;
    private String created;
    private OnlyUsername sender;

    public Comment(String text, String time, OnlyUsername onlyUsername) {
        this.content = text;
        this.created = time;
        this.sender = onlyUsername;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public OnlyUsername getSender() {
        return sender;
    }

    public void setSender(OnlyUsername sender) {
        this.sender = sender;
    }
}
