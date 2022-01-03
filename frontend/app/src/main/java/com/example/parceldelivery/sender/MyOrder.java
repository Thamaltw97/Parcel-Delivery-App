package com.example.parceldelivery.sender;

public class MyOrder {
    private String orderId;
    private String orderDesc;
    private String orderReceiver;
    private String orderStatus;

    public MyOrder(String orderId, String orderDesc, String orderReceiver, String orderStatus) {
        this.orderId = orderId;
        this.orderDesc = orderDesc;
        this.orderReceiver = orderReceiver;
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderReceiver() {
        return orderReceiver;
    }

    public void setOrderReceiver(String orderReceiver) {
        this.orderReceiver = orderReceiver;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
