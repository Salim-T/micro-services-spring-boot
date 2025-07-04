package com.ecommerce.microcommerce.dto;

public class OrderProductRequest {
    private int productId;
    private int quantity = 1;

    public OrderProductRequest() {
    }

    public OrderProductRequest(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderProductRequest{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
