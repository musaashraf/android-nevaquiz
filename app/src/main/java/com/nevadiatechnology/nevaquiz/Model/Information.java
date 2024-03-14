package com.nevadiatechnology.nevaquiz.Model;

public class Information {
    private String oneUID;
    private String oneScore;
    private String oneName;
    private String oneImage;

    private String twoUID;
    private String twoScore;
    private String twoName;
    private String twoImage;

    private String date;

    public Information() {
    }

    public Information(String oneUID, String oneScore, String oneName, String oneImage, String twoUID, String twoScore, String twoName, String twoImage, String date) {
        this.oneUID = oneUID;
        this.oneScore = oneScore;
        this.oneName = oneName;
        this.oneImage = oneImage;
        this.twoUID = twoUID;
        this.twoScore = twoScore;
        this.twoName = twoName;
        this.twoImage = twoImage;
        this.date = date;
    }

    public String getOneUID() {
        return oneUID;
    }

    public void setOneUID(String oneUID) {
        this.oneUID = oneUID;
    }

    public String getOneScore() {
        return oneScore;
    }

    public void setOneScore(String oneScore) {
        this.oneScore = oneScore;
    }

    public String getOneName() {
        return oneName;
    }

    public void setOneName(String oneName) {
        this.oneName = oneName;
    }

    public String getOneImage() {
        return oneImage;
    }

    public void setOneImage(String oneImage) {
        this.oneImage = oneImage;
    }

    public String getTwoUID() {
        return twoUID;
    }

    public void setTwoUID(String twoUID) {
        this.twoUID = twoUID;
    }

    public String getTwoScore() {
        return twoScore;
    }

    public void setTwoScore(String twoScore) {
        this.twoScore = twoScore;
    }

    public String getTwoName() {
        return twoName;
    }

    public void setTwoName(String twoName) {
        this.twoName = twoName;
    }

    public String getTwoImage() {
        return twoImage;
    }

    public void setTwoImage(String twoImage) {
        this.twoImage = twoImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
