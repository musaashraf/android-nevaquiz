package com.nevadiatechnology.nevaquiz.Model;

public class FCMResult {
    private String error;
    private String message_id;

    public FCMResult() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }
}
