
package com.example.capstone.service;

import com.example.capstone.model.request.ChangePasswordRequest;
import com.example.capstone.model.request.DeleteAccountRequest;
import com.example.capstone.model.request.UpdateUserRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;

public class UserService {

  private final ApiClient apiClient;

  public UserService(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  // Update user profile
  public void updateAccount(
          UpdateUserRequest updateUserRequest,
          ApiCallback<Void> callback) {

    ApiRequest request = new ApiRequest("user/updateDetails");

    request.setMethodPUT();
    request.setRequiresAuthentication(true);
    request.setBody(updateUserRequest);

    apiClient.execute(request, Void.class, callback);
  }

  // Change account password
  public void changePassword(
          ChangePasswordRequest changePasswordRequest,
          ApiCallback<Void> callback) {

    ApiRequest request = new ApiRequest("user/changePassword");

    request.setMethodPATCH();
    request.setRequiresAuthentication(true);
    request.setBody(changePasswordRequest);

    apiClient.execute(request, Void.class, callback);
  }

  // Delete user account
  public void deleteAccount(
          DeleteAccountRequest deleteAccountRequest,
          ApiCallback<Void> callback) {
    ApiRequest request = new ApiRequest("user/delete");

    request.setMethodDELETE();
    request.setRequiresAuthentication(true);
    request.setBody(deleteAccountRequest);

    apiClient.execute(request, Void.class, callback);
  }
}
