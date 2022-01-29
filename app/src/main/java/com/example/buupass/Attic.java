package com.example.buupass;

public class Attic {
    //declare the variable
    private String title, desc,postImage, displayName, profilePhoto, time, date;
    //create a constructor

    public Attic(String title, String desc, String postImage,  String time, String date) {
        this.title = title;
        this.desc = desc;
        this.postImage = postImage;



        this.time = time;
        this.date = date;
    }
    //requires an empty constructor

    public Attic() {
    }
    //setters and getters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
