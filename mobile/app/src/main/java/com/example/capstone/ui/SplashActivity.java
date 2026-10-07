package com.example.capstone.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.domainModels.UserRole;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.ui.admin.AdminActivity;
import com.example.capstone.ui.auth.AuthActivity;
import com.example.capstone.ui.user.UserActivity;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;

public class SplashActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    MyApplication app = (MyApplication) getApplication();
    AppContainer container =
        app.getAppContainer();

    PreferenceManager manager = container.preferenceManager;
    SecureSession session = container.secureSession;
    IAuthRepository authRepository = container.authRepository;

    if(session.getSessionExpiration() <= System.currentTimeMillis()){
      if(session.getRefreshExpiration()<= System.currentTimeMillis()){
        session.clear();
        manager.clear();
      }
      else{
        //authRepository.refreshToken();
      }
    }


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