package com.example.jasus.Model;

public class UserModel {
    String userName, userMobile, userDeviceId;

    public UserModel() {
    }

    public UserModel(String userName, String userMobile, String userDeviceId) {
        this.userName = userName;
        this.userMobile = userMobile;
        this.userDeviceId = userDeviceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserDeviceId() {
        return userDeviceId;
    }

    public void setUserDeviceId(String userDeviceId) {
        this.userDeviceId = userDeviceId;
    }
}
