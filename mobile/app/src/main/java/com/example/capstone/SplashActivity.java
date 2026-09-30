package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.capstone.ui.auth.AuthActivity;
import com.example.capstone.util.PreferenceManager;

public class SplashActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    PreferenceManager manager = new PreferenceManager(this);
    if(manager.getRole().toString() == ""){
      startActivity(new Intent(this, MainActivity.class));
    }
    else{
      startActivity(new Intent(this, AuthActivity.class));
    }
    finish();


  }
}