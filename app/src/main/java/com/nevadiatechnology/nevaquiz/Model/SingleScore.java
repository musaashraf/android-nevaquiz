package com.nevadiatechnology.nevaquiz.Model;

public class SingleScore {

    private int score;
    private String id;
    private String categoryName;
    private String uid;

    public SingleScore() {
    }

    public SingleScore(int score, String id, String categoryName, String uid) {
        this.score = score;
        this.id = id;
        this.categoryName = categoryName;
        this.uid = uid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
