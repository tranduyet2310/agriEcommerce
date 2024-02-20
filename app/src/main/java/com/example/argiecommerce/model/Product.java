package com.example.argiecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Product implements Parcelable {
    @SerializedName("id")
    private int productId;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("price")
    private double productPrice;
    @SerializedName("quantity")
    private int productQuantity;
    @SerializedName("supplier")
    private String productSupplier;
    @SerializedName("category")
    private String productCategory;
    @SerializedName("image")
    private String productImage;
    @SerializedName("isFavourite")
    private int isFavourite;
    @SerializedName("isInCart")
    private int isInCart;
    // Child Parcelable object
    private Product mInfo;

    public Product() {
    }

    public Product(String productName, double productPrice, int productQuantity, String productSupplier, String productCategory) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSupplier = productSupplier;
        this.productCategory = productCategory;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeString(productName);
        dest.writeDouble(productPrice);
        dest.writeInt(productQuantity);
        dest.writeString(productSupplier);
        dest.writeString(productCategory);
        dest.writeString(productImage);
        dest.writeInt(isFavourite);
        dest.writeInt(isInCart);
        dest.writeParcelable(mInfo, flags);
    }
    protected Product(Parcel in) {
        productId = in.readInt();
        productName = in.readString();
        productPrice = in.readDouble();
        productQuantity = in.readInt();
        productSupplier = in.readString();
        productCategory = in.readString();
        productImage = in.readString();
        isFavourite = in.readInt();
        isInCart = in.readInt();
        mInfo = in.readParcelable(Product.class.getClassLoader());
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(String productSupplier) {
        this.productSupplier = productSupplier;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite ? 1 : 0;
    }

    public int getIsInCart() {
        return isInCart;
    }

    public void setIsInCart(boolean isInCart) {
        this.isInCart = isInCart ? 1 : 0;
    }

}
