package com.example.parceldelivery.Models;

public class ResViewLoginModel {

    private long userId;
    private String userName;
    private String userType;
    private String token;
    private String userStatus;

    public ResViewLoginModel() {
    }

    public ResViewLoginModel(long userId, String userName, String userType, String token, String userStatus) {
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.token = token;
        this.userStatus = userStatus;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
