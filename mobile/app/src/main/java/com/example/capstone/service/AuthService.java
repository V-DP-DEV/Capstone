package com.example.capstone.service;

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

        apiClient.execute(request, LoginResponse.class, new ApiCallback<LoginResponse>() {

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

    public void signup(SignupRequest signupRequest, ApiCallback<Void> callback) {

        ApiRequest request = new ApiRequest("auth/signup");

        request.setRequiresAuthentication(false);

        request.setBody(signupRequest);

        request.setMethodPost();

        apiClient.execute(request, Void.class, new ApiCallback<Void>() {

            @Override
            public void onSuccess(ApiResponse<Void> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onGeneralError(Exception e) {
                callback.onGeneralError(e);
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                callback.onHttpError(response);
            }
        });
    }

    public void logout(ApiCallback<Void> callback) {

        ApiRequest request = new ApiRequest("auth/logout");

        request.setRequiresAuthentication(false);

        String token = session.getAccessToken();

        request.addAuthHeader(token);

        request.setMethodPost();

        apiClient.execute(request, Void.class, new ApiCallback<Void>() {

            @Override
            public void onSuccess(ApiResponse<Void> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onGeneralError(Exception e) {
                callback.onGeneralError(e);
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                callback.onHttpError(response);
            }
        });
    }


    public void logoutAllDevices(ApiCallback<Void> callback) {

        ApiRequest request = new ApiRequest("auth/logoutAllDevices");

        request.setRequiresAuthentication(false);

        String token = session.getAccessToken();

        request.addAuthHeader(token);

        request.setMethodPost();

        apiClient.execute(request, Void.class, new ApiCallback<Void>() {

            @Override
            public void onSuccess(ApiResponse<Void> response) {
                callback.onSuccess(response);
            }

            @Override
            public void onGeneralError(Exception e) {
                callback.onGeneralError(e);
            }

            @Override
            public void onHttpError(ApiResponse<Void> response) {
                callback.onHttpError(response);
            }
        });
    }

    //only exception to no callback being passed

    public void refresh() {

        String refreshToken = session.getRefreshToken();

        RefreshRequest refreshRequest = new RefreshRequest();
        refreshRequest.setRefreshToken(refreshToken);

        ApiRequest request = new ApiRequest("auth/refresh");

        request.setRequiresAuthentication(false);

        request.setBody(refreshRequest);

        request.setMethodPost();

        apiClient.execute(
                request,
                LoginResponse.class,
                new ApiCallback<LoginResponse>() {

                    @Override
                    public void onSuccess(ApiResponse<LoginResponse> response) {

                        try {
                            LoginResponse loginResponse = response.getData();

                            // Save the new access token
                            session.saveAccessToken(
                                    loginResponse.getToken()
                            );

                            // Save the new refresh token
                            session.saveRefreshToken(
                                    loginResponse.getRefreshToken()
                            );

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onGeneralError(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onHttpError(ApiResponse<LoginResponse> response) {
                        // HTTP error
                    }
                }
        );

    }
}