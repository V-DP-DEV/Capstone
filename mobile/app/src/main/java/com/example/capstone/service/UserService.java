
package com.example.capstone.service;

import com.example.capstone.model.request.ChangePasswordRequest;
import com.example.capstone.model.request.DeleteAccountRequest;
import com.example.capstone.model.request.UpdateUserRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;

public class UserService {

  private final ApiClient apiClient;

  // TODO: Confirm these paths against the deployed backend.
  private static final String UPDATE_PROFILE_PATH = "users/profile";
  private static final String CHANGE_PASSWORD_PATH = null;
  private static final String DELETE_ACCOUNT_PATH = null;

  public UserService(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  // Update user profile
  public void updateAccount(
          UpdateUserRequest updateUserRequest,
          ApiCallback<Void> callback) {

    ApiRequest request = new ApiRequest(UPDATE_PROFILE_PATH);

    request.setMethodPUT();
    request.setRequiresAuthentication(true);
    request.setBody(updateUserRequest);

    apiClient.execute(request, Void.class, callback);
  }

  // Change account password
  public void changePassword(
          ChangePasswordRequest changePasswordRequest,
          ApiCallback<Void> callback) {

    if (CHANGE_PASSWORD_PATH == null) {
      callback.onGeneralError(
              new IllegalStateException(
                      "Change password endpoint not configured"
              )
      );
      return;
    }

    ApiRequest request = new ApiRequest(CHANGE_PASSWORD_PATH);

    request.setMethodPUT();
    request.setRequiresAuthentication(true);
    request.setBody(changePasswordRequest);

    apiClient.execute(request, Void.class, callback);
  }

  // Delete user account
  public void deleteAccount(
          DeleteAccountRequest deleteAccountRequest,
          ApiCallback<Void> callback) {

    if (DELETE_ACCOUNT_PATH == null) {
      callback.onGeneralError(
              new IllegalStateException(
                      "Delete account endpoint not configured"
              )
      );
      return;
    }

    ApiRequest request = new ApiRequest(DELETE_ACCOUNT_PATH);

    request.setMethodDELETE();
    request.setRequiresAuthentication(true);
    request.setBody(deleteAccountRequest);

    apiClient.execute(request, Void.class, callback);
  }
}
