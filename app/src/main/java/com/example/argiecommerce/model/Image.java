package com.example.argiecommerce.model;

public class Image {
    private long id;
    private String imageUrl;
    private String name;
    private String imageId;

    public Image() {
    }

    public Image(long id, String imageUrl, String name, String imageId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.imageId = imageId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
