package com.example.drivefest.data.model;

import java.time.LocalDate;
import java.util.Map;

public class Event extends EventShort{
    private String description;
    private String locationCords;

    public Event(){
        description = null;
        locationCords = null;
    }

    public Event(String id, String name, String image, LocalDate date, String location, int followersCount, String[] tags,
                 String description, String place){
        super(id, name, image, date, location, followersCount, tags);
        this.description = description;
        this.locationCords = place;
    }
    public Event(EventShort eventShort){
        super(eventShort.getId(),
                eventShort.getName(),
                eventShort.getImage(),
                eventShort.getDate(),
                eventShort.getLocation(),
                eventShort.getFollowersCount(),
                eventShort.getTags());
    }



    public void setData(Map<String, Object> document){
        description = document.get("description").toString();
        locationCords = document.get("location").toString();
    }

    public String getDescription() {
        return description;
    }

    public String getLocationCords() {
        return locationCords;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocationCords(String location) {
        this.locationCords = location;
    }
}
