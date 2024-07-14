package com.example.drivefest.data.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Expense {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String type;
    private String description;
    private float price;
    private String dateStr;

    public LocalDate getDate() {
        return date;
    }

    private LocalDate date;


    public Expense(String id, String type, String description, float price, String dateStr, LocalDate date) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.price = price;
        this.dateStr = dateStr;
        this.date = date;
    }

    public Expense() {
        this.id = null;
        this.type = null;
        this.description = null;
        this.price = 0;
        this.dateStr = null;
        this.date = null;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        setDate(LocalDate.parse(dateStr, formatter));
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setData(Map<String, Object> map, String id){
        this.id = id;
        this.type = map.get("type").toString();
        this.description =  map.get("description").toString();
        float price = Float.parseFloat(map.get("price").toString());
        this.price = Math.round(price * 100.0f) / 100.0f;
        this.dateStr =  map.get("date").toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.date = LocalDate.parse(map.get("date").toString(), formatter);
    }
}
