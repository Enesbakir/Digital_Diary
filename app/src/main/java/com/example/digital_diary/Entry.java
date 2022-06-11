package com.example.digital_diary;

import android.os.Parcelable;

import java.io.Serializable;

public class Entry implements Serializable {

    private String title;
    private String enrtyText;
    private String password;
    private String location;
    private String date;
    private String mediaPath;
    private String ıd;
    private double latidude;
    private double longtidude;

    public Entry(String title, String enrtyText, String location, String date,String password) {
        this.title = title;
        this.enrtyText = enrtyText;
        this.location = location;
        this.date = date;
        this.password= password;
    }

    public Entry(String title, String enrtyText, String location, String date,String password,String ıd,String mediaPath) {
        this.title = title;
        this.enrtyText = enrtyText;
        this.password = password;
        this.location = location;
        this.date = date;
        this.ıd = ıd;
        this.mediaPath=mediaPath;
    }
    public Entry(String title, String enrtyText, String location, String date,String password,String ıd,String mediaPath,double latidude,double longtidude) {
        this.title = title;
        this.enrtyText = enrtyText;
        this.password = password;
        this.location = location;
        this.date = date;
        this.ıd = ıd;
        this.mediaPath=mediaPath;
        this.longtidude=longtidude;
        this.latidude = latidude;
    }
    public double getLatidude() {
        return latidude;
    }

    public void setLatidude(double latidude) {
        this.latidude = latidude;
    }

    public double getLongtidude() {
        return longtidude;
    }

    public void setLongtidude(double longtidude) {
        this.longtidude = longtidude;
    }

    public String getId() {
        return ıd;
    }

    public void setId(String ıd) {
        this.ıd = ıd;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEnrtyText() {
        return enrtyText;
    }

    public void setEnrtyText(String enrtyText) {
        this.enrtyText = enrtyText;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }


}
