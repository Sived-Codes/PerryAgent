package com.perry.jasus.Models;

public class SMS_Model {
    String Sendernum,message,currentTime,phone_number;

    public SMS_Model(String sendernum, String message, String currentTime) {
        Sendernum = sendernum;
        this.message = message;
        this.currentTime = currentTime;
        this.phone_number = phone_number;
    }

    public String getSendernum() {
        return Sendernum;
    }

    public void setSendernum(String sendernum) {
        Sendernum = sendernum;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
