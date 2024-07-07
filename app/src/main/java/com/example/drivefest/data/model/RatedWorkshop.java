package com.example.drivefest.data.model;

import java.util.Map;

public class RatedWorkshop {
    private String userId;
    private String workshopId;
    private int rate;

    public RatedWorkshop(String userId, String workshopId, int rate) {
        this.userId = userId;
        this.workshopId = workshopId;
        this.rate = rate;
    }

    public RatedWorkshop() {
        this.userId = null;
        this.workshopId = null;
        this.rate = 0;
    }

    public String getUserId() {
        return userId;
    }

    public String getWorkshopId() {
        return workshopId;
    }

    public int getRate() {
        return rate;
    }

    public void setData(Map<String, Object> data){
        this.userId = data.get("userId").toString();
        this.workshopId = data.get("workshopId").toString();
        this.rate = Integer.valueOf(data.get("rate").toString());
    }
}
