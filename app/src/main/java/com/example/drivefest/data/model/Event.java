package com.example.drivefest.data.model;

import java.time.LocalDate;
import java.util.Map;

public class Event extends EventShort{
    private String description;
    private String locationCords;
    private String address;

    public Event(){
        description = null;
        locationCords = null;
        address = null;
    }
    public Event(EventShort eventShort){
        super(eventShort.getId(),
                eventShort.getName(),
                eventShort.getImage(),
                eventShort.getDate(),
                eventShort.getLocation(),
                eventShort.getFollowersCount(),
                eventShort.getTags(),
                eventShort.getVoivodeship(),
                eventShort.getIsFollowed());
    }

    public void setData(Map<String, Object> document){
        description = document.get("description").toString();
        locationCords = document.get("location").toString();
        address = document.get("address").toString();
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
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
