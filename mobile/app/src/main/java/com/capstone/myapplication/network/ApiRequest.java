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
  private static final String baseurl = "https://aceitapi.co.za/api/";
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private String url;
  private Map<String,String> headers = new HashMap<>();
  private String method;
  private String body = null;

  public ApiRequest(String url){
    this.url = url;
    headers.put("Accept","application/json");
  }

  public void execute(ApiCallback callback){
    new Thread(() -> {
      try {
        ApiResponse response = performRequest();
        mainHandler.post(()->{
          if(response.isHttpError()){
            callback.onHttpError(response);
          }

          else if(response.isSuccess()){
            callback.onSuccess(response);
          }
          else{
            
          }
        });
      } catch (Exception e) {
        mainHandler.post(()->{callback.onGeneralError(e);});
      }

    }).start();
  }

  private ApiResponse performRequest() throws Exception{
    URL url = new URL(
        baseurl+getUrl()
    );

    HttpURLConnection connection =
        (HttpURLConnection) url.openConnection();

    connection.setRequestMethod(getMethod());

    for (Map.Entry<String, String> header : headers.entrySet()) {
      connection.setRequestProperty(
          header.getKey(),
          header.getValue()
      );
    }

    connection.setConnectTimeout(5000);
    connection.setReadTimeout(5000);

    if(body!=null){
      connection.setRequestProperty(
          "Content-Type","application/json"
      );
      connection.setDoOutput(true);

      try (OutputStream output =
               connection.getOutputStream()) {

        output.write(getBody().getBytes(StandardCharsets.UTF_8));
      }
    }


    int responseCode = connection.getResponseCode();

    InputStream inputStream;

    if (responseCode >= 200 && responseCode < 300) {
      inputStream = connection.getInputStream();
    } else {
      inputStream = connection.getErrorStream();
    }

    BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(
                inputStream
            )
        );

    StringBuilder response = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
      response.append(line);
    }

    reader.close();
    connection.disconnect();

    System.out.println("Status: " + responseCode);
    System.out.println("Response: " + response);

    return new ApiResponse(responseCode,response.toString());
  }

  public String getUrl() {
    return url;
  }
  public String getBody() {
    return body;
  }

  public String getMethod() {
    return method;
  }

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

  public void setBody(String body) {
    this.body = body;
  }

  public void addAuthHeader(String token){
    headers.put("Authorization","Bearer " + token);
  }
}
