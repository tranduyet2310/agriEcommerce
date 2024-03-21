package com.example.argiecommerce.model;

import java.util.List;

public class CategoryApiResponse {
    private int id;
    private String categoryName;
    private String categoryImage;
    private List<Subcategory> subCategoryList;

    public CategoryApiResponse() {
    }

    public CategoryApiResponse(int id, String categoryName, String categoryImage, List<Subcategory> subCategoryList) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.subCategoryList = subCategoryList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public List<Subcategory> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<Subcategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
}
