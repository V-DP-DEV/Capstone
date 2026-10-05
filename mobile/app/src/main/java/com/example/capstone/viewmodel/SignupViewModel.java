package com.example.capstone.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.IAuthRepository;

import java.lang.invoke.MutableCallSite;

import kotlin.MustUseReturnValue;

public class SignupViewModel extends ViewModel {
  private final IAuthRepository authRepository;

  private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
  private final MutableLiveData<Boolean> signUpSuccess = new MutableLiveData<>(false);
  private final MutableLiveData<String> generalError = new MutableLiveData<>();
  private final MutableLiveData<String> passwordError = new MutableLiveData<>();
  private final MutableLiveData<String> emailError = new MutableLiveData<>();

  private final MutableLiveData<String> confirmPasswordError = new MutableLiveData<>();
  private final MutableLiveData<String> firsntameError = new MutableLiveData<>();
  private final MutableLiveData<String> surnameError = new MutableLiveData<>();
  private final MutableLiveData<String> password =new MutableLiveData<>();
  private final MutableLiveData<String> email = new MutableLiveData<>();
  private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
  private final MutableLiveData<String> firstname = new MutableLiveData<>();
  private final MutableLiveData<String> surname = new MutableLiveData<>();


  public MutableLiveData<String> getConfirmPasswordError() {
    return confirmPasswordError;
  }

  public MutableLiveData<String> getFirsntameError() {
    return firsntameError;
  }

  public MutableLiveData<String> getSurnameError() {
    return surnameError;
  }
  public MutableLiveData<String> getEmail() {
    return email;
  }

  public MutableLiveData<Boolean> getSignUpSuccess() {
    return signUpSuccess;
  }

  public MutableLiveData<String> getPassword() {
    return password;
  }

  public MutableLiveData<String> getGeneralError() {
    return generalError;
  }

  public SignupViewModel(IAuthRepository authRepository){
    this.authRepository = authRepository;
  }
  public LiveData<Boolean> getLoading(){
    return loading;
  }
  public LiveData<String> getPasswordError(){
    return passwordError;
  }
  public LiveData<String> getEmailError(){
    return emailError;
  }
  public void setEmail(String value){
    email.setValue(value);
  }

  public void setPassword(String value){
    password.setValue(value);
  }

  public void setConfirmPassword(String value){
    confirmPassword.setValue(value);
  }

  public void setFirstname(String value){
    firstname.setValue(value);
  }

  public void setSurname(String value){
    surname.setValue(value);
  }

  public void signup(){

  }
}
