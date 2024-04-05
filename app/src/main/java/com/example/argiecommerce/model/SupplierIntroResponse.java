package com.example.argiecommerce.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupplierIntroResponse {
    @SerializedName("id")
    private long id;
    @SerializedName("description")
    private String description;
    @SerializedName("type")
    private String type;
    @SerializedName("supplierId")
    private long supplierId;
    @SerializedName("images")
    private List<Image> images;

    public SupplierIntroResponse() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
