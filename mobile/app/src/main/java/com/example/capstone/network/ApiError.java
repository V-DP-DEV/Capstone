package com.example.capstone.network;

import java.util.Map;

public class ApiError {
  private String code;
  private String message;
  private Map<String, String > details;

  public String getCode(){
    return code;
  }
  public String getMessage(){
    return message;
  }
  public Map<String,String> getDetails(){
    return details;
  }
  //400 is both normal validaiton and field_validation_error for multiple field errors
  public boolean isFieldValidationError(){
    return "FIELD_VALIDATION_ERROR".equals(code);
  }
}
