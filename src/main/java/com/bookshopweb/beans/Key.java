package com.bookshopweb.beans;

import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Key {
    private long id;
    private String publicKey;
    private long userId;
    private LocalDateTime createAt;
    private LocalDateTime expirationAt;

    public Key(long id, String publicKey, long userId, LocalDateTime createAt, LocalDateTime expirationAt) {
        this.id = id;
        this.publicKey = publicKey;
        this.userId = userId;
        this.createAt = createAt;
        this.expirationAt = expirationAt;
    }

    public Key(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Key.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("userId='" + userId + "'")
                .add("publicKey='" + publicKey + "'")
                .add("createdAt='" + createAt + "'")
                .add("expirationAt='" + expirationAt + "'")
                .toString();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long idUser) {
        this.userId = idUser;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getExpirationAt() {
        return expirationAt;
    }

    public void setExpirationAt(LocalDateTime expirationAt) {
        this.expirationAt = expirationAt;
    }
}
