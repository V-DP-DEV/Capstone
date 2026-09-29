package com.capstone.myapplication.network;

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

  //execute
  public void execute(ApiCallback callback){
    //run off main thread
    new Thread(() -> {
      //try
      try {
        //perform the request
        ApiResponse response = performRequest();
        //if response received, handle back on main thread
        mainHandler.post(()->{
          //if http error return onHttpError event
          if(response.isHttpError()){
            callback.onHttpError(response);
          }

          //if success return onSuccess
          else if(response.isSuccess()){
            callback.onSuccess(response);
          }
          else{
            
          }
        });
      //if any other errors occured handle as network errors
      } catch (Exception e) {
        mainHandler.post(()->{callback.onGeneralError(e);});
      }
    //start thread
    }).start();
  }

  private ApiResponse performRequest() throws Exception{
    //create url
    URL url = new URL(
        baseurl+getUrl()
    );

    //open connection
    HttpURLConnection connection =
        (HttpURLConnection) url.openConnection();

    //set request method
    connection.setRequestMethod(getMethod());

    //add all the headers to the request header with key pair value
    for (Map.Entry<String, String> header : headers.entrySet()) {
      connection.setRequestProperty(
          header.getKey(),
          header.getValue()
      );
    }

    //set time out
    connection.setConnectTimeout(5000);
    connection.setReadTimeout(5000);

    //if body is not null 
    if(body!=null){
      //set propery
      connection.setRequestProperty(
          "Content-Type","application/json"
      );
      connection.setDoOutput(true);

      //write body output
      try (OutputStream output =
               connection.getOutputStream()) {

        output.write(getBody().getBytes(StandardCharsets.UTF_8));
      }
    }

    //get response code from connection
    int responseCode = connection.getResponseCode();

    InputStream inputStream;

    //if response get valid stream
    if (responseCode >= 200 && responseCode < 300) {
      inputStream = connection.getInputStream();
    }
    //if response error get error stream
    else {
      inputStream = connection.getErrorStream();
    }

    //read from the stream
    BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(
                inputStream
            )
        );

    //build output
    StringBuilder response = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
      response.append(line);
    }

    //close reader and close connection
    reader.close();
    connection.disconnect();

    //log the response
    System.out.println("Status: " + responseCode);
    System.out.println("Response: " + response);

    //return api response for execute to handle and call correct event
    return new ApiResponse(responseCode,response.toString());
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
  public void addAuthHeader(String token){
    headers.put("Authorization","Bearer " + token);
  }
}
