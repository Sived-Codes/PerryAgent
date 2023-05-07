package com.perry.jasus.Models;

public class CallHistoryModel {

    String type, callerNumber,  receiverNumber;
    int duration;

    public CallHistoryModel() {
    }

    public CallHistoryModel(String type, String callerNumber, String receiverNumber, int duration) {
        this.type = type;
        this.callerNumber = callerNumber;
        this.receiverNumber = receiverNumber;
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
