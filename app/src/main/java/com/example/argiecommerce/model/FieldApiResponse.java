package com.example.argiecommerce.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FieldApiResponse {
    @SerializedName("id")
    private long id;
    @SerializedName("season")
    private String season;
    @SerializedName("cropsName")
    private String cropsName;
    @SerializedName("cropsType")
    private String cropsType;
    @SerializedName("area")
    private String area;
    @SerializedName("estimateYield")
    private Double estimateYield;
    @SerializedName("fieldDetails")
    private ArrayList<FieldDetail> fieldDetails;
    @SerializedName("estimatePrice")
    private long estimatePrice;
    @SerializedName("isComplete")
    private boolean isComplete;

    public FieldApiResponse() {
    }

    public long getEstimatePrice() {
        return estimatePrice;
    }

    public void setEstimatePrice(long estimatePrice) {
        this.estimatePrice = estimatePrice;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getCropsName() {
        return cropsName;
    }

    public void setCropsName(String cropsName) {
        this.cropsName = cropsName;
    }

    public String getCropsType() {
        return cropsType;
    }

    public void setCropsType(String cropsType) {
        this.cropsType = cropsType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Double getEstimateYield() {
        return estimateYield;
    }

    public void setEstimateYield(Double estimateYield) {
        this.estimateYield = estimateYield;
    }

    public ArrayList<FieldDetail> getFieldDetails() {
        return fieldDetails;
    }

    public void setFieldDetails(ArrayList<FieldDetail> fieldDetails) {
        this.fieldDetails = fieldDetails;
    }
}
