package com.example.drivefest.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class Workshop implements Parcelable{
    private String id;
    private String name;
    private String image;
    private String location;
    private float rating;
    private String[] tags;
    private String voivodeship;
    private boolean rated;
    private int ratingCount;
    private int rate;

    public int getRate() {
        return rate;
    }

    public Workshop(String id, String name, String image, String location, float rating, String[] tags, String voivodeship, int ratesCount, boolean rated, int rate) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.location = location;
        this.rating = rating;
        this.tags = tags;
        this.voivodeship = voivodeship;
        this.ratingCount = ratesCount;
        this.rated = rated;
        this.rate = rate;
    }
    public Workshop(){
        this.id = null;
        this.name = null;
        this.image = null;
        this.location = null;
        this.rating = 0;
        this.tags = null;
        this.voivodeship = null;
        this.rated = false;
        this.ratingCount = 0;
        this.rate = 0;
    }

    public void setRated(int rate) {
        this.rated = true;
        this.rate = rate;
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

    public String getLocation() {
        return location;
    }

    public float getRating() {
        return rating;
    }

    public String[] getTags() {
        return tags;
    }

    public String getVoivodeship() {
        return voivodeship;
    }
    public int getRatingCount() {
        return ratingCount;
    }

    public boolean isRated() {
        return rated;
    }
    public void setData(Map<String, Object> document, String id){
        this.id = id;
        name = document.get("name").toString();
        image = document.get("image").toString();
        location = document.get("location").toString();
        rating = Float.valueOf(document.get("rating").toString());
        List<String> tagsList = (List<String>) document.get("tags");
        tags = tagsList.toArray(new String[0]);
        voivodeship = document.get("voivodeship").toString();
        ratingCount = Integer.valueOf(document.get("ratesCount").toString());
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
        dest.writeString(location);
        dest.writeFloat(rating);
        dest.writeStringArray(tags);
        dest.writeString(voivodeship);
    }

    protected Workshop(Parcel in){
        id = in.readString();
        name = in.readString();
        image = in.readString();
        location = in.readString();
        rating = in.readFloat();
        tags = in.createStringArray();
        voivodeship = in.readString();
    }

    public static final Parcelable.Creator<Workshop> CREATOR = new Parcelable.Creator<Workshop>() {
        @Override
        public Workshop createFromParcel(Parcel in) {
            return new Workshop(in);
        }
        @Override
        public Workshop[] newArray(int size) {
            return new Workshop[size];
        }
    };

    public void setRating(){
        rated = true;
    }
}
