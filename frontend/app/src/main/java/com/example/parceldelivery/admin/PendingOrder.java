package com.example.parceldelivery.admin;

public class PendingOrder {

    private String orderId;
    private String orderDesc;
    private String orderAddress;
    private String orderStatus;

    public PendingOrder(String orderId, String orderDesc, String orderAddress, String orderStatus) {
        this.orderId = orderId;
        this.orderDesc = orderDesc;
        this.orderAddress = orderAddress;
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

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

}
