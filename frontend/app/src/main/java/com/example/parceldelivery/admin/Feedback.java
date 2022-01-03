package com.example.parceldelivery.admin;

public class Feedback {

    private String senderName;
    private String feedbackDesc;

    public Feedback(String senderName, String feedbackDesc) {
        this.senderName = senderName;
        this.feedbackDesc = feedbackDesc;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getFeedbackDesc() {
        return feedbackDesc;
    }

    public void setFeedbackDesc(String feedbackDesc) {
        this.feedbackDesc = feedbackDesc;
    }
}
