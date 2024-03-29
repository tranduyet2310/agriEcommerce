package com.example.argiecommerce.model;

public class InfoDialog {
    private String fullName;
    private String cropsType;
    private String state;

    public InfoDialog() {
    }

    public InfoDialog(String fullName, String cropsType, String state) {
        this.fullName = fullName;
        this.cropsType = cropsType;
        this.state = state;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCropsType() {
        return cropsType;
    }

    public void setCropsType(String cropsType) {
        this.cropsType = cropsType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
