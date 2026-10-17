package com.example.capstone.network;

import org.json.JSONException;
import org.json.JSONObject;

/*defines general response for GSON to return
with error object if attached
and data which is generic for specific class responses
*/

public class ApiResponse<T>{
  private int statusCode;
  private boolean success;
  private T data;
  private ApiError error;

  //getters setters
  public int getStatusCode(){
    return statusCode;
  }
  public void setStatusCode(int statusCode){this.statusCode = statusCode;}
  public T getData(){
    return data;
  }
  public ApiError getError(){
    return error;
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

  public boolean isUnauthorized(){
    return statusCode == 401;
  }
  public boolean isForbidden(){
    return statusCode ==403;
  }

  public boolean isNotFound(){
    return statusCode == 404;
  }
  public boolean isServerError(){
    return statusCode >= 500;
  }
  public boolean isValidationError(){
    return statusCode == 400;
  }
}
