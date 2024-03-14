package com.nevadiatechnology.nevaquiz.Model;

public class SubjectCategory {

    private String alpa;
    private String name;
    private int id;

    public SubjectCategory() {
    }

    public SubjectCategory(String alpa, String name, int id) {
        this.alpa = alpa;
        this.name = name;
        this.id = id;
    }

    public String getAlpa() {
        return alpa;
    }

    public void setAlpa(String alpa) {
        this.alpa = alpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
