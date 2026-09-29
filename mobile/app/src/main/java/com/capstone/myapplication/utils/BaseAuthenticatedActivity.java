package com.capstone.myapplication.utils;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseAuthenticatedActivity extends AppCompatActivity {

    protected RoleManager roleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roleManager = new RoleManager(this);

        if (!roleManager.enforceAuthentication()) {
            return;
        }
    }
}