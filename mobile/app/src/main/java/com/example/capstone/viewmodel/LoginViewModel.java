package com.example.capstone.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.capstone.repository.AuthRepository;
import com.example.capstone.repository.IAuthRepository;

import java.lang.invoke.MutableCallSite;

public class LoginViewModel extends ViewModel {
  private final IAuthRepository authRepository;

  private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
  private final MutableLiveData<String> generalError = new MutableLiveData<>();
  private final MutableLiveData<String> passwordError = new MutableLiveData<>();
  private final MutableLiveData<String> emailError = new MutableLiveData<>();
  private final MutableLiveData<String> email = new MutableLiveData<>("");

  private final MutableLiveData<String> password = new MutableLiveData<>("");
  private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>(false);


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

  public LoginViewModel(IAuthRepository authRepository){
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
  public void login(){

  }
}
