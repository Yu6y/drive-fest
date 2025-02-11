package com.example.drivefest.data.model;

public class Image {
    private String name;
    private String url;

    public Image() {
        name = null;
        url = "";
    }

    public Image(String name, String url){
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
