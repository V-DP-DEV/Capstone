package com.example.capstone.model.request;

import android.os.Build;

import java.util.UUID;

public class LoginRequest {
  private String email;
  private String password;
  private String deviceId;

  public String getDeviceId() {
    return deviceId;
  }

  public String getDeviceName() {
    return deviceName;
  }

  private String deviceName;

  public void setDeviceId(){
    this.deviceId = UUID.randomUUID().toString();
  }
  public void setDeviceName(){
    this.deviceName = Build.MANUFACTURER + " " + Build.MODEL;
  }
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
