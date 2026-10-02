package com.example.capstone.network;

import java.util.Map;

public class ApiError {
  private String code;
  private String message;
  private Map<String, String > fields;

  public String getCode(){
    return code;
  }
  public String getMessage(){
    return message;
  }
  public Map<String,String> getFields(){
    return fields;
  }
}
