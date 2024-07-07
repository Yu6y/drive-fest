package com.example.drivefest.data.model;


import com.google.firebase.Timestamp;

import java.util.Map;


public class Comment {
    private String id;
    private String content;
    private String image;
    private String userId;
    private String userName;
    private Timestamp timestamp;

    public Comment(String id, String content, String image, String userId, String userName, Timestamp timestamp) {
        this.id = id;
        this.content = content;
        this.image = image;
        this.userId = userId;
        this.userName = userName;
        this.timestamp = timestamp;
    }
    public Comment(String content, String image, String userId, String userName) {
        this.content = content;
        this.image = image;
        this.userId = userId;
        this.userName = userName;
        this.timestamp = Timestamp.now();
    }

    public Comment() {
        this.id = null;
        this.content = null;
        this.image = null;
        this.userId = null;
        this.userName = null;
        this.timestamp = null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setData(Map<String, Object> data){
        this.content = data.get("content").toString();
        this.image = data.get("image").toString();
        this.userId = data.get("userId").toString();
        this.userName = data.get("userName").toString();
        this.timestamp = (Timestamp) data.get("timestamp");
    }
}
