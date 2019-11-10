package com.example.schoolevents.Models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class SchoolClub {

    private String id;
    private String name;
    private String about;
    private String description;
    private Drawable profilePhoto;
    private ArrayList<Photo> eventPhotos;
    private ArrayList<Event> events;
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Drawable getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Drawable profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public ArrayList<Photo> getEventPhotos() {
        return eventPhotos;
    }

    public void setEventPhotos(ArrayList<Photo> eventPhotos) {
        this.eventPhotos = eventPhotos;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
