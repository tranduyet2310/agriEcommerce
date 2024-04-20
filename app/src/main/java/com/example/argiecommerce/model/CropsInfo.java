package com.example.argiecommerce.model;

public class CropsInfo {
    private long id;
    private String cropsName;
    private Double estimateYield;
    private Double currentYield;
    private long estimatePrice;

    public CropsInfo(String cropsName, Double estimateYield) {
        this.cropsName = cropsName;
        this.estimateYield = estimateYield;
    }

    public Double getCurrentYield() {
        return currentYield;
    }

    public void setCurrentYield(Double currentYield) {
        this.currentYield = currentYield;
    }

    public long getEstimatePrice() {
        return estimatePrice;
    }

    public void setEstimatePrice(long estimatePrice) {
        this.estimatePrice = estimatePrice;
    }

    public CropsInfo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCropsName() {
        return cropsName;
    }

    public void setCropsName(String cropsName) {
        this.cropsName = cropsName;
    }

    public Double getEstimateYield() {
        return estimateYield;
    }

    public void setEstimateYield(Double estimateYield) {
        this.estimateYield = estimateYield;
    }
}
