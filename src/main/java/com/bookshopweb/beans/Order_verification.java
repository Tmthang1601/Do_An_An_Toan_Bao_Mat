package com.bookshopweb.beans;

import java.time.LocalDateTime;

public class Order_verification {
    private long id ;
    private long orderId;
    private String hashcode;
    private String signature;
    public LocalDateTime createAt;

    public Order_verification(long id, long orderId, String hashcode, String signature, LocalDateTime createAt) {
        this.id = id;
        this.orderId = orderId;
        this.hashcode = hashcode;
        this.signature = signature;
        this.createAt = createAt;
    }

    public Order_verification() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "order_verification{" +
                "id=" + id +
                ", order_id=" + orderId +
                ", hashcode='" + hashcode + '\'' +
                ", signature='" + signature + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
