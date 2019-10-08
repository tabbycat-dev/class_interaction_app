package com.example.classinteraction;

import androidx.annotation.NonNull;

import java.util.Date;

public class Checkin {
    private Date datetime;
    private String userId;
    private String name;
    private Double latitude;
    private Double longitude;


    public Checkin() {
    }

    public Checkin(Date datetime, String userId, String name, Double latitude, Double longitude) {
        this.datetime = datetime;
        this.name = name;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Date getDatetime() {
        return datetime;
    }

    public String getUserId() {
        return userId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserID: "+userId +", Name: "+name +", Date: "+ datetime.toString()+", Long/Lat:"+longitude+"/"+latitude;
    }
}
