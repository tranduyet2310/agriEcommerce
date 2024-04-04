package com.example.argiecommerce.model;

import java.math.BigDecimal;

public class ReviewRequest {
    private String feedBack;
    private BigDecimal rating;

    public ReviewRequest() {
    }

    public ReviewRequest(String feedBack, BigDecimal rating) {
        this.feedBack = feedBack;
        this.rating = rating;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }
}
