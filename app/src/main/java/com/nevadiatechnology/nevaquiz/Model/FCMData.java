package com.nevadiatechnology.nevaquiz.Model;

import java.util.Map;

public class FCMData {
    private String to;
    private Map<String, String> data;

    public FCMData(String to, Map<String, String> data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
