package com.example.parceldelivery.Models;

public class ReqUpdateUserStatusModel {

    private long userId;
    private String status;

    public ReqUpdateUserStatusModel() {
    }

    public ReqUpdateUserStatusModel(long userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
