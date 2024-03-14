package com.nevadiatechnology.nevaquiz.Model;

public class OnlineUser {

    private boolean isActive;

    public OnlineUser() {
    }

    public OnlineUser(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
