package com.perry.jasus.Models;

public class ActionSmsModel {
    String action, mobile, sms;

    public ActionSmsModel() {


    }

    public ActionSmsModel(String action, String mobile, String sms) {
        this.action = action;
        this.mobile = mobile;
        this.sms = sms;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}
