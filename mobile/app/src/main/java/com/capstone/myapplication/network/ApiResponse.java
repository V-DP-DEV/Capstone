package com.capstone.myapplication.network;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponse{
  private int statusCode;
  private boolean success;
  private String data;
  private String errorMessage;
  private String errorCode;
  
  //tries to get response, but throws json exception if not valid
  public ApiResponse(int statusCode,String response) throws JSONException{
    //set statusCode
    this.statusCode=statusCode;
    //get response as json object
    JSONObject json = new JSONObject(response);
    //extract success
    success = json.getBoolean("success");
    //try to extract data if not null
    data = json.isNull("data") ? null :json.getJSONObject("data").toString();
    //try to extract error if not null
    JSONObject error = json.isNull("error") ? null : json.getJSONObject("error");
    //if error is not null
    if(error !=null){
      //try to get error message and code if not null
      errorMessage = error.isNull("message")?null :json.getJSONObject("error").getString("message");
      errorCode = error.isNull("code")?null :json.getString("code");
    }
  }

  //getters setters
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
  //determine if http error using codes
  public boolean isHttpError(){
    if (statusCode >= 400 && statusCode < 600) {
      return true;
    }
    return false;
  }
}
