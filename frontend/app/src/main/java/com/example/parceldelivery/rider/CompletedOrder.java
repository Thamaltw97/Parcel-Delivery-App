package com.example.parceldelivery.rider;

public class CompletedOrder {

    private String orderDesc;
    private String orderAddress;

    public CompletedOrder(String orderDesc, String orderAddress) {
        this.orderDesc = orderDesc;
        this.orderAddress = orderAddress;
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
}
