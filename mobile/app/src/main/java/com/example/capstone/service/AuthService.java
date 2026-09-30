package com.example.capstone.service;

import android.content.Context;
import android.os.Build;


import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiRequest;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.util.SecureSession;

import org.json.JSONObject;

import java.util.UUID;

public class AuthService {

  public static void login(
      Context context,
      String email,
      String password,
      ApiCallback callback
  ) {
    ApiRequest request = new ApiRequest("auth/login");

    try {
      String deviceId = UUID.randomUUID().toString();
      String deviceName = Build.MANUFACTURER + " " + Build.MODEL;

      JSONObject body = new JSONObject();
      body.put("email", email);
      body.put("password", password);
      body.put("deviceId", deviceId);
      body.put("deviceName", deviceName);

      request.setBody(body.toString());
      request.setMethodPost();

      request.execute(new ApiCallback() {

        @Override
        public void onSuccess(ApiResponse response) {
          try {
            JSONObject json = new JSONObject(response.getData());

            String token = json.getString("token");

            SecureSession session =
                new SecureSession(context);

            session.saveToken(token);

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