package com.nevadiatechnology.nevaquiz.Model;

public class Request {

    private String category;
    private int status;
    private String senderUID;
    private String senderName;
    private String senderImage;
    private String receiverUID;
    private String receiverName;
    private String receiverImage;

    public Request() {
    }

    public Request(String category, String senderUID, String senderName, String senderImage, String receiverUID, String receiverName, String receiverImage) {
        this.category = category;
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.receiverUID = receiverUID;
        this.receiverName = receiverName;
        this.receiverImage = receiverImage;
        this.status = 0;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getReceiverUID() {
        return receiverUID;
    }

    public void setReceiverUID(String receiverUID) {
        this.receiverUID = receiverUID;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }
}
