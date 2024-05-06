package com.example.drivefest.data.model;

import java.time.LocalDate;

public class EventShort {
    private String id;
    private String name;
    private String image; // Uri?
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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }


}
