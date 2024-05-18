package com.example.drivefest.data.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventShort {
    private String id;
    private String name;
    private String image;
    private LocalDate date;
    private String location;
    private int followersCount;
    private String eventId;
    private String[] tags;

    public EventShort(String id, String name, String image, LocalDate date, String location, int followersCount, String eventId, String[] tags) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.location = location;
        this.followersCount = followersCount;
        this.eventId = eventId;
        this.tags = tags;
    }

    public EventShort(){
        this.id = null;
        this.name = null;
        this.image = null;
        this.date = null;
        this.location = null;
        this.followersCount = 0;
        this.eventId = null;
        this.tags = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public String getEventId() {
        return eventId;
    }

    public String[] getTags() {
        return tags;
    }
    public void setData(Map<String, Object> document){
        name = document.get("name").toString();
        image = document.get("image").toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        date = LocalDate.parse(document.get("date").toString(), formatter);
        location = document.get("location").toString();
        followersCount = Integer.valueOf(document.get("followersCount").toString());
    }

    @Override
    public String toString(){
        return "name: " + name + "location: " + location;
    }

    public void setImage(String image){
        this.image = image;
    }
}
