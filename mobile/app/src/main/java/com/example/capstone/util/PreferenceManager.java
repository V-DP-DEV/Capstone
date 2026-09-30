package com.example.capstone.util;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_NAME = "settings";
    private static final String KEY_ROLE = "role";
    private static final String KEY_SESSION_EXPIRATION = "session_expiration";

    private final SharedPreferences preferences;

    public PreferenceManager(Context context) {
        preferences = context.getApplicationContext()
            .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setRole(String role) {
        preferences.edit()
            .putString(KEY_ROLE, role)
            .apply();
    }

    public String getRole() {
        return preferences.getString(KEY_ROLE, "");
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
