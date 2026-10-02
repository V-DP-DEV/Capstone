package com.example.capstone.domainModels;

import java.io.Serializable;

public class User implements Serializable {

    private long userId;
    private String email;
    private String firstName;
    private String surname;
    private UserRole role;
    private long createdAt;

    public User() {
    }

    public User(long userId, String email, String firstName, String surname, UserRole role, long createdAt) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.role = role;
        this.createdAt = createdAt;
    }

    public long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String fullName) { this.firstName = firstName; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

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