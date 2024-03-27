package com.example.argiecommerce.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product implements Parcelable {
    @SerializedName("id")
    private long productId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("description")
    private String description;
    @SerializedName("standardPrice")
    private Long standardPrice;
    @SerializedName("discountPrice")
    private Long discountPrice;
    @SerializedName("quantity")
    private int productQuantity;
    @SerializedName("categoryName")
    private String productCategory;
    @SerializedName("subCategoryName")
    private String productSubcategory;
    @SerializedName("warehouseName")
    private String warehouseName;
    @SerializedName("supplierShopName")
    private String productSupplier;
    @SerializedName("images")
    private List<Image> productImage;
    @SerializedName("active")
    private boolean isActive;
    @SerializedName("available")
    private boolean isAvailable;
    @SerializedName("new")
    private boolean isNew;
    @SerializedName("supplierProvince")
    private String supplierProvince;
    @SerializedName("supplierId")
    private long supplierId;
    @SerializedName("sold")
    private long sold;
    private int isFavourite;
    private int isInCart;

    public Product() {
    }

    public Product(long productId, String productName, String description, Long standardPrice, Long discountPrice, int productQuantity, String productCategory, String productSubcategory, String warehouseName, String productSupplier, List<Image> productImage, boolean isActive, boolean isAvailable, boolean isNew, String supplierProvince, long supplierId, long sold, int isFavourite, int isInCart) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.standardPrice = standardPrice;
        this.discountPrice = discountPrice;
        this.productQuantity = productQuantity;
        this.productCategory = productCategory;
        this.productSubcategory = productSubcategory;
        this.warehouseName = warehouseName;
        this.productSupplier = productSupplier;
        this.productImage = productImage;
        this.isActive = isActive;
        this.isAvailable = isAvailable;
        this.isNew = isNew;
        this.supplierProvince = supplierProvince;
        this.supplierId = supplierId;
        this.sold = sold;
        this.isFavourite = isFavourite;
        this.isInCart = isInCart;
    }

    protected Product(Parcel in) {
        productId = in.readLong();
        productName = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            standardPrice = null;
        } else {
            standardPrice = in.readLong();
        }
        if (in.readByte() == 0) {
            discountPrice = null;
        } else {
            discountPrice = in.readLong();
        }
        productQuantity = in.readInt();
        productCategory = in.readString();
        productSubcategory = in.readString();
        warehouseName = in.readString();
        productSupplier = in.readString();
        isActive = in.readByte() != 0;
        isAvailable = in.readByte() != 0;
        isNew = in.readByte() != 0;
        supplierProvince = in.readString();
        supplierId = in.readLong();
        sold = in.readLong();
        isFavourite = in.readInt();
        isInCart = in.readInt();
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(Long standardPrice) {
        this.standardPrice = standardPrice;
    }

    public Long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductSubcategory() {
        return productSubcategory;
    }

    public void setProductSubcategory(String productSubcategory) {
        this.productSubcategory = productSubcategory;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(String productSupplier) {
        this.productSupplier = productSupplier;
    }

    public List<Image> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<Image> productImage) {
        this.productImage = productImage;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getSupplierProvince() {
        return supplierProvince;
    }

    public void setSupplierProvince(String supplierProvince) {
        this.supplierProvince = supplierProvince;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getSold() {
        return sold;
    }

    public void setSold(long sold) {
        this.sold = sold;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public int getIsInCart() {
        return isInCart;
    }

    public void setIsInCart(int isInCart) {
        this.isInCart = isInCart;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(productId);
        dest.writeString(productName);
        dest.writeString(description);
        if (standardPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(standardPrice);
        }
        if (discountPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(discountPrice);
        }
        dest.writeInt(productQuantity);
        dest.writeString(productCategory);
        dest.writeString(productSubcategory);
        dest.writeString(warehouseName);
        dest.writeString(productSupplier);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeByte((byte) (isNew ? 1 : 0));
        dest.writeString(supplierProvince);
        dest.writeLong(supplierId);
        dest.writeLong(sold);
        dest.writeInt(isFavourite);
        dest.writeInt(isInCart);
    }
}
