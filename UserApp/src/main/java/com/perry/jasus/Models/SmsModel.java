package com.perry.jasus.Models;

public class SmsModel {
    String Number, Msg, Date;

    public SmsModel() {
    }

    public SmsModel(String number, String msg, String date) {
        Number = number;
        Msg = msg;
        Date = date;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
