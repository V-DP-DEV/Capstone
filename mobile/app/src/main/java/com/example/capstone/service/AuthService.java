package com.example.capstone.service;

import android.content.Context;
import android.os.Build;
import android.util.Log;


import com.example.capstone.model.request.LoginRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.util.SecureSession;

import org.json.JSONObject;

import java.util.UUID;
import java.util.prefs.AbstractPreferences;

public class AuthService {
  private final ApiClient apiClient;
  private final SecureSession session;
  public AuthService(ApiClient apiClient,SecureSession session){
    this.apiClient = apiClient;
    this.session = session;
  }
  public void login(
      LoginRequest loginRequest,
      ApiCallback callback
  ) {
    ApiRequest request = new ApiRequest("auth/login");

    try {

      JSONObject body = new JSONObject();
      body.put("email", loginRequest.getEmail());
      body.put("password", loginRequest.getPassword());
      body.put("deviceId", loginRequest.getDeviceId());
      body.put("deviceName", loginRequest.getDeviceName());

      request.setBody(body.toString());
      request.setMethodPost();

      apiClient.execute(request,new ApiCallback() {

        @Override
        public void onSuccess(ApiResponse response) {
          try {
            JSONObject json = new JSONObject(response.getData());

            String token = json.getString("token");

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
        public void onHttpError(ApiResponse response) {
          callback.onHttpError(response);
        }
      });

    } catch (Exception e) {
      callback.onGeneralError(e);
    }
  }
}