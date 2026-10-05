package com.example.capstone.service;

import android.content.Context;
import android.os.Build;
import android.util.Log;


import com.example.capstone.model.request.LoginRequest;
import com.example.capstone.model.request.RefreshRequest;
import com.example.capstone.model.request.SignupRequest;
import com.example.capstone.model.response.LoginResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiError;
import com.example.capstone.network.ApiRequest;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;

import org.json.JSONObject;

import java.util.UUID;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.PreferenceChangeEvent;

public class AuthService {
  private final ApiClient apiClient;
  private final SecureSession session;
  private final PreferenceManager prefManager;
  public AuthService(ApiClient apiClient,SecureSession session,PreferenceManager prefManager){
    this.apiClient = apiClient;
    this.session = session;
    this.prefManager = prefManager;
  }
  public void login(
      LoginRequest loginRequest,
      ApiCallback<LoginResponse> callback
  ) {
    ApiRequest request = new ApiRequest("auth/login");
    request.setRequiresAuthentication(false);
    request.setBody(loginRequest);
    request.setMethodPost();

    apiClient.execute(request,LoginResponse.class,new ApiCallback<LoginResponse>() {

      @Override
      public void onSuccess(ApiResponse<LoginResponse> response) {
        try {
          LoginResponse loginResponse = response.getData();

          String token = loginResponse.getToken();
          session.saveAccessToken(token);

          // Pass the original response back to the screen
          callback.onSuccess(response);

        } catch (Exception e) {
          callback.onGeneralError(e);
        }
      }

      @Override
      public void onGeneralError(Exception e) {
          callback.onGeneralError(e);
        }
        @Override

      public void onHttpError(ApiResponse<LoginResponse> response) {
        callback.onHttpError(response);
      }
      });
  }

  public void signup(SignupRequest signupRequest, ApiCallback<Void> callback){

  }
  public void logout(ApiCallback<Void> callback){

  }

  public void logoutAllDevices(ApiCallback<Void> callback){

  }
  //only exception to no callback being passed
  public void refresh(){

  }
}