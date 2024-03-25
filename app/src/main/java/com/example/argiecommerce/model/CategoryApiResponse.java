package com.example.argiecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class CategoryApiResponse implements Parcelable {
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

    protected CategoryApiResponse(Parcel in) {
        id = in.readInt();
        categoryName = in.readString();
        categoryImage = in.readString();
    }

    public static final Creator<CategoryApiResponse> CREATOR = new Creator<CategoryApiResponse>() {
        @Override
        public CategoryApiResponse createFromParcel(Parcel in) {
            return new CategoryApiResponse(in);
        }

        @Override
        public CategoryApiResponse[] newArray(int size) {
            return new CategoryApiResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(categoryName);
        dest.writeString(categoryImage);
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
