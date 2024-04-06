package com.example.argiecommerce.model;

public class CropsInfo {
    private long id;
    private String cropsName;
    private Double estimateYield;

    public CropsInfo(String cropsName, Double estimateYield) {
        this.cropsName = cropsName;
        this.estimateYield = estimateYield;
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
