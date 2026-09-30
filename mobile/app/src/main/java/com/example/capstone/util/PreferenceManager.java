package com.example.capstone.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.capstone.domainModels.UserRole;

public class PreferenceManager {

    private static final String PREF_NAME = "settings";
    private static final String KEY_ROLE = "role";
    private static final String KEY_SESSION_EXPIRATION = "session_expiration";

    private final SharedPreferences preferences;

    public PreferenceManager(Context context) {
        preferences = context.getApplicationContext()
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setRole(UserRole role) {
        preferences.edit()
            .putString(KEY_ROLE, role.name())
            .apply();
    }

    public UserRole getRole() {
        String role = preferences.getString(KEY_ROLE, "");
        return UserRole.valueOf(role);
    }

    public void setSessionExpiration(long expiration) {
        preferences.edit()
            .putLong(KEY_SESSION_EXPIRATION, expiration)
            .apply();
    }

    public long getSessionExpiration() {
        return preferences.getLong(KEY_SESSION_EXPIRATION, 0L);
    }
}
