package com.nevadiatechnology.nevaquiz.Model;

public class User {

    private String name;
    private String uid;
    private String email;
    private String image;

    public User() {
    }

    public User(String name, String uid, String email, String image) {
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
