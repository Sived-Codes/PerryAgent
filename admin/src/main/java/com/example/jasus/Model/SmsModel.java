package com.example.jasus.Model;

public class SmsModel {
    String currentTime
            , message
            , sendernum
            ;

    public SmsModel() {
    }

    public SmsModel(String currentTime, String message, String sendernum) {
        this.currentTime = currentTime;
        this.message = message;
        this.sendernum = sendernum;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendernum() {
        return sendernum;
    }

    public void setSendernum(String sendernum) {
        this.sendernum = sendernum;
    }
}



