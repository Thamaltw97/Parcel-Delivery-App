package com.example.parceldelivery.Models;

public class ReqUpdateOrderStatusModel {

    private long orderId;
    private String status;

    public ReqUpdateOrderStatusModel() {
    }

    public ReqUpdateOrderStatusModel(long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
