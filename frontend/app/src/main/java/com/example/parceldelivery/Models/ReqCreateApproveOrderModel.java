package com.example.parceldelivery.Models;

public class ReqCreateApproveOrderModel {

    private String approvalStatus;
    private Double price;
    private String remarks;
    private long orderId;
    private long userId;
    private long driverId;

    public ReqCreateApproveOrderModel(String approvalStatus, Double price, String remarks, long orderId, long userId, long driverId) {
        this.approvalStatus = approvalStatus;
        this.price = price;
        this.remarks = remarks;
        this.orderId = orderId;
        this.userId = userId;
        this.driverId = driverId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }
}
