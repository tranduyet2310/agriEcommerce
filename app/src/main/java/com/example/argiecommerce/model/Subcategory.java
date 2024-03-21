package com.example.argiecommerce.model;

public class Subcategory {
    private int id;
    private String subcategoryName;
    private String categoryName;

    public Subcategory() {
    }

    public Subcategory(int id, String subcategoryName, String categoryName) {
        this.id = id;
        this.subcategoryName = subcategoryName;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
