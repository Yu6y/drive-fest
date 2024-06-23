package com.example.drivefest.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventShort implements Parcelable {
    private String id;
    private String name;
    private String image;
    private LocalDate date;
    private String location;
    private int followersCount;
    private String[] tags;
    private String voivodeship;

    public EventShort(String id, String name, String image, LocalDate date, String location, int followersCount, String[] tags, String voivodeship) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.location = location;
        this.followersCount = followersCount;
        this.tags = tags;
        this.voivodeship = voivodeship;
    }

    public EventShort(){
        this.id = null;
        this.name = null;
        this.image = null;
        this.date = null;
        this.location = null;
        this.followersCount = 0;
        this.tags = null;
        this.voivodeship = null;
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


    public String[] getTags() {
        return tags;
    }
    public String getVoivodeship(){
        return voivodeship;
    }
    public void setData(Map<String, Object> document, String id){
        this.id = id;
        name = document.get("name").toString();
        image = document.get("image").toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        date = LocalDate.parse(document.get("date").toString(), formatter);
        location = document.get("location").toString();
        followersCount = Integer.valueOf(document.get("followersCount").toString());
        List<String> tagsList = (List<String>) document.get("tags");
        tags = tagsList.toArray(new String[0]);
        voivodeship = document.get("voivodeship").toString();
    }

    @Override
    public String toString(){
        return "name: " + name + "location: " + location;
    }

    public void setImage(String image){
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeSerializable(date);
        dest.writeString(location);
        dest.writeInt(followersCount);
        dest.writeStringArray(tags);
        dest.writeString(voivodeship);
    }

    protected EventShort(Parcel in){
        id = in.readString();
        name = in.readString();
        image = in.readString();
        date = (LocalDate) in.readSerializable();
        location = in.readString();
        followersCount = in.readInt();
        tags = in.createStringArray();
        voivodeship = in.readString();
    }

    public static final Creator<EventShort> CREATOR = new Creator<EventShort>() {
        @Override
        public EventShort createFromParcel(Parcel in) {
            return new EventShort(in);
        }
        @Override
        public EventShort[] newArray(int size) {
            return new EventShort[size];
        }
    };
}
