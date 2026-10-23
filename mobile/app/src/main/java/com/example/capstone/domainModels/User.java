package com.example.capstone.domainModels;

/**
 * Domain model representing an authenticated system user profile.
 */
import java.io.Serializable;

public class User implements Serializable {

    private long id;
    private String email;
    private String firstname;
    private String surname;
    private UserRole role;
    private long createdAt;

    public User() {
    }

    public User(long userId, String email, String firstname, String surname, UserRole role, long createdAt) {
        this.id = userId;
        this.email = email;
        this.firstname = firstname;
        this.surname = surname;
        this.role = role;
        this.createdAt = createdAt;
    }

    public long getUserId() { return id; }
    public void setUserId(Long userId) { this.id = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String fullName) { this.firstname = firstname; }

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