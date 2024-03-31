package com.example.argiecommerce.model;

public class FavoriteResponse {
    private Long id;
    private Long userId;
    private Product product;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long userId, Product product) {
        this.id = id;
        this.userId = userId;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
