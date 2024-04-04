package com.example.argiecommerce.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class ReviewResponse {
    @SerializedName("id")
    private Long reviewId;
    @SerializedName("feedBack")
    private String feedBack;
    @SerializedName("rating")
    private BigDecimal rating;
    @SerializedName("reviewDate")
    private String reviewDate;
    @SerializedName("userFullName")
    private String userFullName;
    @SerializedName("productName")
    private String productName;

    public ReviewResponse() {
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
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

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
