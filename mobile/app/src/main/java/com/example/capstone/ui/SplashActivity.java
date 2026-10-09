package com.example.capstone.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.capstone.AppContainer;
import com.example.capstone.MyApplication;
import com.example.capstone.domainModels.InterviewCategory;
import com.example.capstone.domainModels.UserInterview;
import com.example.capstone.domainModels.UserInterviewSummary;
import com.example.capstone.domainModels.UserRole;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.network.RefreshCallback;
import com.example.capstone.network.RefreshError;
import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.repository.IInterviewRepository;
import com.example.capstone.ui.admin.AdminActivity;
import com.example.capstone.ui.auth.AuthActivity;
import com.example.capstone.ui.user.UserActivity;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;
import com.google.gson.Gson;

import java.util.List;

public class SplashActivity extends Activity {
  MyApplication app;
  AppContainer container;

  PreferenceManager manager;
  SecureSession session;
  IAuthRepository authRepository;
  IInterviewRepository interviewRepository;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    app = (MyApplication) getApplication();
    container = app.getAppContainer();

    manager = container.preferenceManager;
    session = container.secureSession;
    authRepository = container.authRepository;
    interviewRepository = container.interviewRepository;

    interviewRepository.getCategories(new ApiCallback<List<InterviewCategory>>() {
      @Override
      public void onSuccess(ApiResponse<List<InterviewCategory>> response) {
        System.out.println(new Gson().toJson(response));
      }

      @Override
      public void onGeneralError(Exception e) {

      }

      @Override
      public void onHttpError(ApiResponse<List<InterviewCategory>> response) {

      }
    });

    long sessionExpiration = session.getSessionExpiration();
    long refreshExpiration = session.getRefreshExpiration();
    long now = System.currentTimeMillis();


    // No session at all
    if (sessionExpiration == 0L) {
      routeToAuth();
      return;
    }

    // Access token is still valid
    if (sessionExpiration > now) {
      routeToCorrectDashboard(manager.getRole());
      return;
    }

    // Access token expired and refresh token is also expired
    if (refreshExpiration <= now) {
      clearSessionAndRouteToAuth();
      return;
    }

    // Access token expired, but refresh token is still valid
    authRepository.refreshToken(new RefreshCallback() {

      @Override
      public void onSuccess() {
        System.out.println("Refresh succeeded");
        routeToCorrectDashboard(manager.getRole());
      }

      @Override
      public void onError(RefreshError error) {
        System.out.println(
            "Clearing due to refresh failing: " + error
        );

        clearSessionAndRouteToAuth();
      }
    });
  }

  private void routeToCorrectDashboard(UserRole role) {
    if (role == null) {
      routeToAuth();
      return;
    }

    Intent intent;

    if (role == UserRole.USER) {
      intent = new Intent(this, UserActivity.class);
    } else if (role == UserRole.ADMIN) {
      intent = new Intent(this, AdminActivity.class);
    } else {
      routeToAuth();
      return;
    }

    startActivity(intent);
    finish();
  }

  private void clearSessionAndRouteToAuth() {
    session.clear();
    manager.clear();

    routeToAuth();
  }

  private void routeToAuth() {
    startActivity(new Intent(this, AuthActivity.class));
    finish();
  }
}