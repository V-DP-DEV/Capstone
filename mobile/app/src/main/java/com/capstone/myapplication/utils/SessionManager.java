package com.capstone.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.capstone.myapplication.model.User;
import com.capstone.myapplication.model.UserRole;

public class SessionManager {

    private static final String PREF_NAME = "CapstoneSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getFullName());
        editor.putString(KEY_USER_ROLE, user.getRole().name());
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public UserRole getUserRole() {
        String role = prefs.getString(KEY_USER_ROLE, UserRole.USER.name());
        return UserRole.fromString(role);
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "User");
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public boolean isAdmin() {
        return getUserRole() == UserRole.ADMIN;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}