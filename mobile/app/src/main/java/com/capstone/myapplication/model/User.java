package com.capstone.myapplication.model;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;
    private String email;
    private String fullName;
    private UserRole role;
    private long createdAt;

    public User() {
    }

    public User(String userId, String email, String fullName, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = System.currentTimeMillis();
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public boolean isUser() {
        return role == UserRole.USER;
    }
}