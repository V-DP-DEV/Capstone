package com.example.capstone.util;

public class FormValidator {
    public static boolean isValidEmail(String email) {
        return email != null &&
                email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[^A-Za-z0-9].*");
    }
}
