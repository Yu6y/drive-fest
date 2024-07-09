package com.example.drivefest.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workshop implements Parcelable{
    private String id;
    private String name;
    private String image;
    private String location;
    private String[] tags;
    private String voivodeship;
    private Map<Integer, Integer> ratings;
    private float rate;
    private int rateCount;
    private boolean rated;
    private boolean ratedFromDb;
    private int rateByUser;

    public int getRateCount() {
        return rateCount;
    }

    public void updateRating(int rate, int a){
        this.ratings.put(rate,ratings.get(rate) + 1);
        for(int i = 1; i < 6; i++){
            Log.e("debug " + i + " " + a, String.valueOf(ratings.get(i)));
        }

    }
    public void wypisz(){
        System.out.println("wypsz");
        for(int i = 1; i < 6; i++){
            System.out.println(i + " -- " + ratings.get(i));
        }
    }

    public Workshop(String id, String name, String image, String location, String[] tags, String voivodeship, boolean rated, float rate, Map<Integer, Integer> ratings, int rateCount, boolean ratedFromDb, int rateByUser) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.location = location;
        this.tags = tags;
        this.voivodeship = voivodeship;
        this.rated = rated;
        this.rate = rate;
        this.ratings = ratings;
        this.ratedFromDb = ratedFromDb;
        this.rateCount = rateCount;
        this.rateByUser = rateByUser;
    }

    public boolean isRatedFromDb() {
        return ratedFromDb;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated() {
        this.rated = true;
    }

    public void setRatedFromDb(boolean ratedFromDb) {
        this.ratedFromDb = ratedFromDb;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public Workshop(){
        this.id = null;
        this.name = null;
        this.image = null;
        this.location = null;
        this.tags = null;
        this.voivodeship = null;
        this.rated = false;
        this.rate = 0;
        this.ratings = new HashMap<>();
        this.ratedFromDb = false;
        this.rateCount = 0;
        this.rateByUser = 0;
    }
    public Map<Integer, Integer> getRatings(){
        return ratings;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRateByUser() {
        return rateByUser;
    }

    public void setRateByUser(int rateByUser) {
        this.rateByUser = rateByUser;
    }

    public float getRate() {
        return rate;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }


    public String[] getTags() {
        return tags;
    }

    public String getVoivodeship() {
        return voivodeship;
    }

    public boolean getRated() {
        return rated;
    }
    public void setData(Map<String, Object> document, String id){
        this.id = id;
        name = document.get("name").toString();
        image = document.get("image").toString();
        location = document.get("location").toString();
        Map<String, Long> ratingData = (Map<String, Long>) document.get("ratings");
        for(Map.Entry<String, Long> entry: ratingData.entrySet()){
            Integer key = Integer.valueOf(entry.getKey());
            Integer value = entry.getValue().intValue();
            ratings.put(key, value);
        }
        List<String> tagsList = (List<String>) document.get("tags");
        tags = tagsList.toArray(new String[0]);
        voivodeship = document.get("voivodeship").toString();
        float value = 0;
        for(Integer i = 1; i < 6; i++){
            rateCount += ratings.get(i);
            value += i * ratings.get(i);
        }
        if(rateCount == 0)
            rate = 0;
        else
            rate = (float) (Math.round((value / rateCount) * 10) / 10.0);
    }

    @Override
    public String toString(){
        return "name: " + name + "location: " + location;
    }

    public void setImage(String image){
        this.image = image;
    }

    public Map<String, Long> getRatingsForDb(){
        Map<String, Long> ratingData = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : ratings.entrySet()) {
            String key = entry.getKey().toString();
            Long value = entry.getValue().longValue();
            System.out.println(key + " - " + value);
            ratingData.put(key, value);
        }

        return ratingData;
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
        dest.writeMap(ratings);
        dest.writeStringArray(tags);
        dest.writeString(voivodeship);
    }

    protected Workshop(Parcel in){
        id = in.readString();
        name = in.readString();
        image = in.readString();
        location = in.readString();
        in.readMap(ratings, HashMap.class.getClassLoader());
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
}
