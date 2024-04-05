package com.example.argiecommerce.model;

import com.example.argiecommerce.utils.CropsStatus;

public class FieldDetail {
    private long id;
    private CropsStatus cropsStatus;
    private String dateCreated;
    private String details;

    public FieldDetail() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CropsStatus getCropsStatus() {
        return cropsStatus;
    }

    public void setCropsStatus(CropsStatus cropsStatus) {
        this.cropsStatus = cropsStatus;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
