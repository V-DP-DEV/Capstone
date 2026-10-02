package com.example.capstone.network;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ApiRequest {
  //define static url
  private static final String baseurl = "https://aceitapi.co.za/api/";
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private String url;
  private Map<String,String> headers = new HashMap<>();
  private String method;
  private String body = null;

  //create object with request, and put json body in header
  public ApiRequest(String url){
    this.url = url;
    headers.put("Accept","application/json");
  }

  //getters
  public String getUrl() {
    return url;
  }
  public String getBody() {
    return body;
  }

  public String getMethod() {
    return method;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  //setters for method
  public void setMethodPost() {
    this.method = "POST";
  }

  public void setMethodGET(){
    this.method = "GET";
  }

  public void setMethodPUT(){
    this.method = "PUT";
  }

  public void setMethodDELETE(){
    this.method = "DELETE";
  }

  public void setMethodPATCH(){
    this.method = "PATCH";
  }

  //set body
  public void setBody(String body) {
    this.body = body;
  }

  //used to add to auth header
  /*public void addAuthHeader(String token){
    headers.put("Authorization","Bearer " + token);
  }
  */
}
