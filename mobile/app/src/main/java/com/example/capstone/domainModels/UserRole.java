package com.example.capstone.domainModels;

public enum UserRole {
    USER,
    ADMIN;

    public static UserRole fromString(String role) {
        if (role == null) return USER;
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return USER;
        }
    }
}