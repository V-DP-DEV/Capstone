package com.example.TripBuddy.data.helpers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    //constants
    private static final String KEY_PREF_NAME = "settings";
    private static final String KEY_ROLE = "role";
    private static final String KEY_SESSIONS_EXPIRATION = "session_expiration";

    
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    //creates shared preferences
    public static void setSharedPreferences(Context context){
        preferences = context.getApplicationContext().getSharedPreferences(KEY_PREF_NAME,MODE_PRIVATE);
        editor = preferences.edit();
    }

    //setters and getters with appropriate constant keys
    public static void setRole(String role){
        editor.putString(KEY_ROLE,role);
    }
    public static String getRole(){
        return preferences.getString(KEY_ROLE,"");
    }

    public static void setSessionExpiration(long sessionExpiration){
        editor.putLong(KEY_SESSIONS_EXPIRATION,sessionExpiration);
    }
    public static int getSessionExpiration(){
        return preferences.getInt(KEY_SESSIONS_EXPIRATION,0);
    }

    public static void apply(){
        editor.apply();
    }
}
