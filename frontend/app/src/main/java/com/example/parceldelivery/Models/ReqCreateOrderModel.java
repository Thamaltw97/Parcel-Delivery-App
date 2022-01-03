package com.example.parceldelivery.Models;

public class ReqCreateOrderModel {

    private String receiverName;
    private String receiverMobile;
    private String description;
    private String receiveLocation;
    private String pickupLocation;
    private long userId;

    public ReqCreateOrderModel() {
    }

    public ReqCreateOrderModel(String receiverName, String receiverMobile, String description,
                          String receiveLocation, String pickupLocation, long userId) {
        this.receiverName = receiverName;
        this.receiverMobile = receiverMobile;
        this.description = description;
        this.receiveLocation = receiveLocation;
        this.pickupLocation = pickupLocation;
        this.userId = userId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiveLocation() {
        return receiveLocation;
    }

    public void setReceiveLocation(String receiveLocation) {
        this.receiveLocation = receiveLocation;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

}
