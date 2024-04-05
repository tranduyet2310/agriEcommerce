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
    private Long estimateYield;
    @SerializedName("fieldDetails")
    private ArrayList<FieldDetail> fieldDetails;

    public FieldApiResponse() {
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

    public Long getEstimateYield() {
        return estimateYield;
    }

    public void setEstimateYield(Long estimateYield) {
        this.estimateYield = estimateYield;
    }

    public ArrayList<FieldDetail> getFieldDetails() {
        return fieldDetails;
    }

    public void setFieldDetails(ArrayList<FieldDetail> fieldDetails) {
        this.fieldDetails = fieldDetails;
    }
}
