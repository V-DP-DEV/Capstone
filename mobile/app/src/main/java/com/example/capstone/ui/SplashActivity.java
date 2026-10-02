package com.example.capstone.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.capstone.domainModels.UserRole;
import com.example.capstone.ui.admin.AdminActivity;
import com.example.capstone.ui.auth.AuthActivity;
import com.example.capstone.ui.user.UserActivity;
import com.example.capstone.util.PreferenceManager;

public class SplashActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    PreferenceManager manager = new PreferenceManager(this);
manager.setRole(UserRole.USER);
    UserRole role = manager.getRole();
    if(role == null){
      startActivity(new Intent(this, AuthActivity.class));
    }
    else{
      if(role == UserRole.USER){
        startActivity(new Intent(this, UserActivity.class));
      }
      if(role == UserRole.ADMIN){
        startActivity(new Intent(this, AdminActivity.class));
      }
    }
    finish();
  }
}