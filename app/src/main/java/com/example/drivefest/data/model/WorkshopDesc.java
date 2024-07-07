package com.example.drivefest.data.model;

import java.sql.Struct;
import java.util.HashMap;

public class WorkshopDesc extends Workshop{
    private String address;
    private String description;
    private String locationCords;

    public WorkshopDesc(){
        this.address = null;
        this.description = null;
        this.locationCords = null;
    }

    public WorkshopDesc(Workshop workshop, String address, String description, String location){
        super(workshop.getId(),
                workshop.getName(),
                workshop.getImage(),
                workshop.getLocation(),
                workshop.getRating(),
                workshop.getTags(),
                workshop.getVoivodeship(),
                workshop.getRatingCount(),
                workshop.isRated(),
                workshop.getRate());
        this.address = address;
        this.description = description;
        this.locationCords = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocationCords(String locationCords) {
        this.locationCords = locationCords;
    }

    public String getDescription() {
        return description;
    }

    public String getLocationCords() {
        return locationCords;
    }
    public WorkshopDesc(Workshop workshop){
        super(workshop.getId(),
                workshop.getName(),
                workshop.getImage(),
                workshop.getLocation(),
                workshop.getRating(),
                workshop.getTags(),
                workshop.getVoivodeship(),
                workshop.getRatingCount(),
                workshop.isRated(),
                workshop.getRate());
    }

}
