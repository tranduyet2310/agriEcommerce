package com.example.argiecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Subcategory implements Parcelable {
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

    protected Subcategory(Parcel in) {
        id = in.readInt();
        subcategoryName = in.readString();
        categoryName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(subcategoryName);
        dest.writeString(categoryName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Subcategory> CREATOR = new Creator<Subcategory>() {
        @Override
        public Subcategory createFromParcel(Parcel in) {
            return new Subcategory(in);
        }

        @Override
        public Subcategory[] newArray(int size) {
            return new Subcategory[size];
        }
    };

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
