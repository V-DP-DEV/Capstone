package com.example.capstone.service;

import com.example.capstone.model.request.LoginRequest;
import com.example.capstone.model.request.RefreshRequest;
import com.example.capstone.model.request.SignupRequest;
import com.example.capstone.model.response.LoginResponse;
import com.example.capstone.model.response.RefreshResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.util.DateTime;
import com.example.capstone.util.PreferenceManager;
import com.example.capstone.util.SecureSession;

public class AuthService {

    private final ApiClient apiClient;
    private final SecureSession session;
    private final PreferenceManager prefManager;

    public AuthService(
            ApiClient apiClient,
            SecureSession session,
            PreferenceManager prefManager
    ) {
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

        apiClient.execute(
                request,
                LoginResponse.class,
                new ApiCallback<LoginResponse>() {

                    @Override
                    public void onSuccess(ApiResponse<LoginResponse> response) {
                        try {
                            LoginResponse loginResponse = response.getData();

                            session.saveAccessToken(
                                    loginResponse.getToken()
                            );

                            session.saveRefreshToken(
                                    loginResponse.getRefreshToken()
                            );

                            session.setSessionExpiration(
                                    loginResponse.getTokenExpiresAt()
                            );

                            session.setRefreshExpiration(
                                    loginResponse.getRefreshTokenExpiresAt()
                            );

                            if (loginResponse.getUserRole() != null) {
                                prefManager.setRole(
                                        com.example.capstone.domainModels.UserRole.valueOf(
                                                loginResponse.getUserRole()
                                        )
                                );
                            }

                            session.setLoggedIn(true);

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
                    public void onHttpError(
                            ApiResponse<LoginResponse> response
                    ) {
                        callback.onHttpError(response);
                    }
                }
        );
    }

    public void signup(
            SignupRequest signupRequest,
            ApiCallback<LoginResponse> callback
    ) {
        ApiRequest request = new ApiRequest("auth/signup");
        request.setRequiresAuthentication(false);
        request.setBody(signupRequest);
        request.setMethodPost();

        apiClient.execute(
                request,
                LoginResponse.class,
                new ApiCallback<LoginResponse>() {

                    @Override
                    public void onSuccess(ApiResponse<LoginResponse> response) {
                        try {
                            LoginResponse signupResponse = response.getData();

                            session.saveAccessToken(
                                    signupResponse.getToken()
                            );

                            session.saveRefreshToken(
                                    signupResponse.getRefreshToken()
                            );

                            session.setSessionExpiration(
                                    DateTime.utcToLocalMillis(
                                            signupResponse.getTokenExpiresAt()
                                    )
                            );

                            session.setRefreshExpiration(
                                    DateTime.utcToLocalMillis(
                                            signupResponse.getRefreshTokenExpiresAt()
                                    )
                            );

                            if (signupResponse.getUserRole() != null) {
                                prefManager.setRole(
                                        com.example.capstone.domainModels.UserRole.valueOf(
                                                signupResponse.getUserRole()
                                        )
                                );
                            }

                            session.setLoggedIn(true);

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
                }
        );
    }


    public void logout(ApiCallback<Void> callback) {

        String accessToken = session.getAccessToken();

        ApiRequest request = new ApiRequest("auth/logout");
        request.setRequiresAuthentication(false);
        request.addAuthHeader(accessToken);
        request.setMethodPost();

        apiClient.execute(
                request,
                Void.class,
                new ApiCallback<Void>() {

                    @Override
                    public void onSuccess(ApiResponse<Void> response) {
                        session.clear();
                        prefManager.clear();
                        session.setLoggedIn(false);

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
                }
        );
    }

    public void logoutAllDevices(ApiCallback<Void> callback) {

        String accessToken = session.getAccessToken();

        ApiRequest request = new ApiRequest("auth/logoutAllDevices");
        request.setRequiresAuthentication(false);
        request.addAuthHeader(accessToken);
        request.setMethodPost();

        apiClient.execute(
                request,
                Void.class,
                new ApiCallback<Void>() {

                    @Override
                    public void onSuccess(ApiResponse<Void> response) {
                        session.clear();
                        prefManager.clear();
                        session.setLoggedIn(false);

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
                }
        );
    }

    // Only exception to no callback being passed
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
                RefreshResponse.class,
                new ApiCallback<RefreshResponse>() {

                    @Override
                    public void onSuccess(
                            ApiResponse<RefreshResponse> response
                    ) {
                        try {
                            RefreshResponse refreshResponse =
                                    response.getData();

                            session.saveAccessToken(
                                    refreshResponse.getToken()
                            );

                            session.saveRefreshToken(
                                    refreshResponse.getRefreshToken()
                            );

                            session.setSessionExpiration(
                                    refreshResponse.getTokenExpiresAt()
                            );

                            session.setRefreshExpiration(
                                    refreshResponse.getRefreshTokenExpiresAt()
                            );

                            if (refreshResponse.getUserRole() != null) {
                                prefManager.setRole(
                                        com.example.capstone.domainModels.UserRole.valueOf(
                                                refreshResponse.getUserRole()
                                        )
                                );
                            }

                        } catch (Exception e) {
                            session.clear();
                            prefManager.clear();
                            session.setLoggedIn(false);
                        }
                    }

                    @Override
                    public void onGeneralError(Exception e) {
                        session.clear();
                        prefManager.clear();
                        session.setLoggedIn(false);
                    }

                    @Override
                    public void onHttpError(
                            ApiResponse<RefreshResponse> response
                    ) {
                        session.clear();
                        prefManager.clear();
                        session.setLoggedIn(false);
                    }
                }
        );
    }
}