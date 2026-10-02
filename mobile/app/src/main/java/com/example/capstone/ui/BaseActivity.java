package com.example.capstone.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstone.MyApplication;
import com.example.capstone.ui.auth.AuthActivity;
import com.example.capstone.util.SecureSession;

public abstract class BaseActivity extends AppCompatActivity {

  protected SecureSession secureSession;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    MyApplication app =
        (MyApplication) getApplication();

    secureSession =
        app.getAppContainer().getSecureSession();

    secureSession.getLoggedIn()
        .observe(this, expired -> {

          if (Boolean.FALSE.equals(expired)) {
            redirectToLogin();
          }

        });
  }

  private void redirectToLogin() {

    secureSession.clear();

    Intent intent = new Intent(
        this,
        AuthActivity.class
    );

    intent.setFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK |
            Intent.FLAG_ACTIVITY_CLEAR_TASK
    );

    startActivity(intent);
    finish();
  }
}