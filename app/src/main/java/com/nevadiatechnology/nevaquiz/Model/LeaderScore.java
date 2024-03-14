package com.nevadiatechnology.nevaquiz.Model;

public class LeaderScore {

    private String score, name, image, id, subjectName;

    public LeaderScore() {
    }

    public LeaderScore(String score, String name, String image, String id, String subjectName) {
        this.score = score;
        this.name = name;
        this.image = image;
        this.id = id;
        this.subjectName = subjectName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
