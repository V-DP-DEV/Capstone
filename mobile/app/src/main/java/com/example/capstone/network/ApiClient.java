package com.example.capstone.network;

import android.os.Handler;
import android.os.Looper;

import com.example.capstone.util.SecureSession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ApiClient {
  private static final String baseurl = "https://aceitapi.co.za/api/";
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private final SecureSession secureSession;
  private final Gson gson;

  public ApiClient(SecureSession secureSession,Gson gson){
    this.secureSession = secureSession;
    this.gson = gson;
  }

  public <T> void execute(ApiRequest request, Type responseType, ApiCallback<T> callback){
    //run off main thread
    new Thread(() -> {
      //try
      try {
        //perform the request
        ApiResponse<T> response = performRequest(request,responseType);
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

  private <T> ApiResponse<T> performRequest(ApiRequest request,Type responseType) throws Exception{
    //create url
    URL url = new URL(
        baseurl+request.getUrl()
    );

    //open connection
    HttpURLConnection connection =
        (HttpURLConnection) url.openConnection();

    //set request method
    connection.setRequestMethod(request.getMethod());

    //add all the headers to the request header with key pair value
    for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
      connection.setRequestProperty(
          header.getKey(),
          header.getValue()
      );
    }

    //set time out
    connection.setConnectTimeout(5000);
    connection.setReadTimeout(5000);

    //if body is not null
    if (request.getBody() != null) {

      String jsonBody = gson.toJson(request.getBody());

      connection.setRequestProperty(
          "Content-Type",
          "application/json"
      );

      connection.setDoOutput(true);

      try (OutputStream output = connection.getOutputStream()) {
        output.write(
            jsonBody.getBytes(StandardCharsets.UTF_8)
        );
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
    StringBuilder responseBody = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }

    //close reader and close connection
    reader.close();
    connection.disconnect();

    //log the response
    System.out.println("Status: " + responseCode);
    System.out.println("Response: " + responseBody);

    ApiResponse<T> response = gson.fromJson(responseBody.toString(),TypeToken.getParameterized(ApiResponse.class,responseType).getType());
    response.setStatusCode(responseCode);
    return response;

    //return api response for execute to handle and call correct event
  }
}
