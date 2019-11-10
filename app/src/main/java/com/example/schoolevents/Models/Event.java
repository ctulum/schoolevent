package com.example.schoolevents.Models;

import java.util.ArrayList;

public class Event {

    private String id;
    private String name;
    private Photo photo;
    private ArrayList<Photo> eventPhotos;
    private String description;
    private String keys;
    private String date;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public ArrayList<Photo> getEventPhotos() {
        return eventPhotos;
    }

    public void setEventPhotos(ArrayList<Photo> eventPhotos) {
        this.eventPhotos = eventPhotos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
