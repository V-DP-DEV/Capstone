package com.capstone.myapplication.network;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponse{

  public ApiResponse(int statusCode,String response) throws JSONException{
    this.statusCode=statusCode;
    JSONObject json = new JSONObject(response);
    success = json.getBoolean("success");
    data = json.isNull("data") ? null :json.getJSONObject("data").toString();
    JSONObject error = json.isNull("error") ? null : json.getJSONObject("error");
    if(error !=null){
      errorMessage = error.isNull("message")?null :json.getJSONObject("error").getString("message");
      errorCode = error.isNull("code")?null :json.getString("code");
    }


  }
  private int statusCode;
  private boolean success;
  private String data;
  private String errorMessage;
  private String errorCode;
  public int getStatusCode(){
    return statusCode;
  }
  public String getData(){
    return data;
  }
  public String getErrorMessage(){
    return errorMessage;
  }
  public String getErrorCode(){
    return errorCode;
  }
  public boolean isSuccess(){
    return success;
  }
  public boolean isHttpError(){
    if (statusCode >= 400 && statusCode < 600) {
      return true;
    }
    return false;
  }
}
