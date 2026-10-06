package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.model.request.LoginRequest;
import com.example.capstone.model.response.LoginResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.util.FormValidator;

import java.util.Map;

public class LoginViewModel extends ViewModel {

  private final IAuthRepository authRepository;

  private final MutableLiveData<Boolean> loading =
          new MutableLiveData<>(false);

  private final MutableLiveData<String> generalError =
          new MutableLiveData<>();

  private final MutableLiveData<String> passwordError =
          new MutableLiveData<>();

  private final MutableLiveData<String> emailError =
          new MutableLiveData<>();

  private final MutableLiveData<String> email =
          new MutableLiveData<>("");

  private final MutableLiveData<String> password =
          new MutableLiveData<>("");

  private final MutableLiveData<Boolean> loginSuccess =
          new MutableLiveData<>(false);


  public LoginViewModel(IAuthRepository authRepository) {
    this.authRepository = authRepository;
  }


  public MutableLiveData<String> getEmail() {
    return email;
  }

  public MutableLiveData<Boolean> getLoginSuccess() {
    return loginSuccess;
  }

  public MutableLiveData<String> getPassword() {
    return password;
  }

  public MutableLiveData<String> getGeneralError() {
    return generalError;
  }

  public LiveData<Boolean> getLoading() {
    return loading;
  }

  public LiveData<String> getPasswordError() {
    return passwordError;
  }

  public LiveData<String> getEmailError() {
    return emailError;
  }


  public void setEmail(String value) {
    email.setValue(value);
  }

  public void setPassword(String value) {
    password.setValue(value);
  }


  public void login() {

    emailError.setValue(null);
    passwordError.setValue(null);
    generalError.setValue(null);

    boolean validEmail =
            FormValidator.isValidEmail(email.getValue());

    boolean validPassword =
            FormValidator.isValidPassword(password.getValue());

    if (!validEmail) {
      emailError.setValue(
              "Please enter a valid email address."
      );
    }

    if (!validPassword) {
      passwordError.setValue(
              "Password must be at least 8 characters and contain a number and special character."
      );
    }

    if (!validEmail || !validPassword) {
      return;
    }

    LoginRequest request = new LoginRequest();

    request.setEmail(email.getValue());
    request.setPassword(password.getValue());

    request.setDeviceId();
    request.setDeviceName();

    loading.setValue(true);

    authRepository.login(
            request,
            new ApiCallback<LoginResponse>() {

              @Override
              public void onSuccess(
                      ApiResponse<LoginResponse> response
              ) {
                loading.setValue(false);
                loginSuccess.setValue(true);
              }

              @Override
              public void onGeneralError(Exception e) {
                loading.setValue(false);

                generalError.setValue(
                        "Something went wrong. Please try again."
                );
              }

              @Override
              public void onHttpError(
                      ApiResponse<LoginResponse> response
              ) {
                loading.setValue(false);

                if (response != null
                        && response.isValidationError()
                        && response.getError() != null
                        && response.getError().isFieldValidationError()) {

                  Map<String, String> details =
                          response.getError().getDetails();

                  if (details != null) {

                    if (details.containsKey("email")) {
                      emailError.setValue(
                              details.get("email")
                      );
                    }

                    if (details.containsKey("password")) {
                      passwordError.setValue(
                              details.get("password")
                      );
                    }
                  }

                  return;
                }

                if (response != null
                        && response.getError() != null) {

                  generalError.setValue(
                          response.getError().getMessage()
                  );

                } else {

                  generalError.setValue(
                          "Login failed. Please check your details and try again."
                  );
                }
              }
            }
    );
  }
}