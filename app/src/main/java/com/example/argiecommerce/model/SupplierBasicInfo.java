package com.example.argiecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SupplierBasicInfo implements Parcelable {
    private String imageUrl;
    private Long supplierId;
    private String supplierProvince;
    private String supplierShopName;
    private Double rating;

    public SupplierBasicInfo() {
    }

    protected SupplierBasicInfo(Parcel in) {
        imageUrl = in.readString();
        if (in.readByte() == 0) {
            supplierId = null;
        } else {
            supplierId = in.readLong();
        }
        supplierProvince = in.readString();
        supplierShopName = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
    }

    public static final Creator<SupplierBasicInfo> CREATOR = new Creator<SupplierBasicInfo>() {
        @Override
        public SupplierBasicInfo createFromParcel(Parcel in) {
            return new SupplierBasicInfo(in);
        }

        @Override
        public SupplierBasicInfo[] newArray(int size) {
            return new SupplierBasicInfo[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierProvince() {
        return supplierProvince;
    }

    public void setSupplierProvince(String supplierProvince) {
        this.supplierProvince = supplierProvince;
    }

    public String getSupplierShopName() {
        return supplierShopName;
    }

    public void setSupplierShopName(String supplierShopName) {
        this.supplierShopName = supplierShopName;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(imageUrl);
        if (supplierId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(supplierId);
        }
        dest.writeString(supplierProvince);
        dest.writeString(supplierShopName);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
    }
}
