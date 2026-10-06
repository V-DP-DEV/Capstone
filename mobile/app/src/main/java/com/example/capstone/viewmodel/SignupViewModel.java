package com.example.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.model.request.SignupRequest;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiResponse;
import com.example.capstone.repository.IAuthRepository;
import com.example.capstone.util.FormValidator;

import java.util.Map;

public class SignupViewModel extends ViewModel {

  private final IAuthRepository authRepository;

  private final MutableLiveData<Boolean> loading =
          new MutableLiveData<>(false);

  private final MutableLiveData<Boolean> signUpSuccess =
          new MutableLiveData<>(false);

  private final MutableLiveData<String> generalError =
          new MutableLiveData<>();

  private final MutableLiveData<String> passwordError =
          new MutableLiveData<>();

  private final MutableLiveData<String> emailError =
          new MutableLiveData<>();

  private final MutableLiveData<String> confirmPasswordError =
          new MutableLiveData<>();

  private final MutableLiveData<String> firstnameError =
          new MutableLiveData<>();

  private final MutableLiveData<String> surnameError =
          new MutableLiveData<>();

  private final MutableLiveData<String> password =
          new MutableLiveData<>("");

  private final MutableLiveData<String> email =
          new MutableLiveData<>("");

  private final MutableLiveData<String> confirmPassword =
          new MutableLiveData<>("");

  private final MutableLiveData<String> firstname =
          new MutableLiveData<>("");

  private final MutableLiveData<String> surname =
          new MutableLiveData<>("");


  public SignupViewModel(IAuthRepository authRepository) {
    this.authRepository = authRepository;
  }


  public LiveData<String> getFirstname() {
    return firstname;
  }

  public LiveData<String> getSurname() {
    return surname;
  }

  public LiveData<String> getEmail() {
    return email;
  }

  public LiveData<String> getPassword() {
    return password;
  }

  public LiveData<String> getConfirmPassword() {
    return confirmPassword;
  }


  public LiveData<String> getFirstnameError() {
    return firstnameError;
  }

  public LiveData<String> getSurnameError() {
    return surnameError;
  }

  public LiveData<String> getEmailError() {
    return emailError;
  }

  public LiveData<String> getPasswordError() {
    return passwordError;
  }

  public LiveData<String> getConfirmPasswordError() {
    return confirmPasswordError;
  }

  public LiveData<String> getGeneralError() {
    return generalError;
  }

  public LiveData<Boolean> getLoading() {
    return loading;
  }

  public LiveData<Boolean> getSignUpSuccess() {
    return signUpSuccess;
  }


  public void setEmail(String value) {
    email.setValue(value);
  }

  public void setPassword(String value) {
    password.setValue(value);
  }

  public void setConfirmPassword(String value) {
    confirmPassword.setValue(value);
  }

  public void setFirstname(String value) {
    firstname.setValue(value);
  }

  public void setSurname(String value) {
    surname.setValue(value);
  }


  public void signup() {

    firstnameError.setValue(null);
    surnameError.setValue(null);
    emailError.setValue(null);
    passwordError.setValue(null);
    confirmPasswordError.setValue(null);
    generalError.setValue(null);

    boolean validFirstname =
            firstname.getValue() != null
                    && !firstname.getValue().trim().isEmpty();

    boolean validSurname =
            surname.getValue() != null
                    && !surname.getValue().trim().isEmpty();

    boolean validEmail =
            FormValidator.isValidEmail(email.getValue());

    boolean validPassword =
            FormValidator.isValidPassword(password.getValue());

    boolean validConfirmPassword =
            password.getValue() != null
                    && password.getValue().equals(
                    confirmPassword.getValue()
            );


    if (!validFirstname) {
      firstnameError.setValue(
              "First name is required."
      );
    }

    if (!validSurname) {
      surnameError.setValue(
              "Surname is required."
      );
    }

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

    if (!validConfirmPassword) {
      confirmPasswordError.setValue(
              "Passwords do not match."
      );
    }


    if (!validFirstname
            || !validSurname
            || !validEmail
            || !validPassword
            || !validConfirmPassword) {
      return;
    }


    SignupRequest request = new SignupRequest();

    request.setFirstname(firstname.getValue());
    request.setSurname(surname.getValue());
    request.setEmail(email.getValue());
    request.setPassword(password.getValue());

    loading.setValue(true);

    authRepository.signup(
            request,
            new ApiCallback<Void>() {

              @Override
              public void onSuccess(
                      ApiResponse<Void> response
              ) {
                loading.setValue(false);
                signUpSuccess.setValue(true);
              }

              @Override
              public void onGeneralError(
                      Exception e
              ) {
                loading.setValue(false);

                generalError.setValue(
                        "Something went wrong. Please try again."
                );
              }

              @Override
              public void onHttpError(
                      ApiResponse<Void> response
              ) {
                loading.setValue(false);

                if (response != null
                        && response.isValidationError()
                        && response.getError() != null
                        && response.getError().isFieldValidationError()) {

                  Map<String, String> details =
                          response.getError().getDetails();

                  if (details != null) {

                    if (details.containsKey("firstname")) {
                      firstnameError.setValue(
                              details.get("firstname")
                      );
                    }

                    if (details.containsKey("surname")) {
                      surnameError.setValue(
                              details.get("surname")
                      );
                    }

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

                    if (details.containsKey("confirmPassword")) {
                      confirmPasswordError.setValue(
                              details.get("confirmPassword")
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
                          "Unable to create your account. Please try again."
                  );
                }
              }
            }
    );
  }
}