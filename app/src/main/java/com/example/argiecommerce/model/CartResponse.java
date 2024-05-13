package com.example.argiecommerce.model;

import java.util.Objects;

public class CartResponse {
    private Long id;
    private Long userId;
    private Integer quantity;
    private Product product;

    public CartResponse() {
    }

    public CartResponse(Long id, Long userId, Integer quantity, Product product) {
        this.id = id;
        this.userId = userId;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartResponse)) return false;
        CartResponse that = (CartResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getQuantity(), that.getQuantity()) && Objects.equals(getProduct(), that.getProduct());
    }

}
