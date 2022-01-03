package com.example.parceldelivery.Models;

public class ReqCreateFeedbackModel {

    private String feedback;
    private long orderId;
    private long userId;


    public ReqCreateFeedbackModel() {
    }

    public ReqCreateFeedbackModel(String feedback, long orderId, long userId) {
        this.feedback = feedback;
        this.orderId = orderId;
        this.userId = userId;
    }


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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

}
