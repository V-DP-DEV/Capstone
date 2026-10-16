package com.example.capstone.network;

import android.os.Handler;
import android.os.Looper;

import com.example.capstone.repository.AuthRepository;
import com.example.capstone.util.SecureSession;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;

public class ApiClient {
  private static final String baseurl = "https://aceitapi.co.za/api/";
  private final Handler mainHandler = new Handler(Looper.getMainLooper());
  private final SecureSession secureSession;
  private final Gson gson;
  private final int CONNECT_TIMEOUT_MS =10000;
  private final int READ_TIMEOUT_MS = 30000;
  private Consumer<RefreshCallback> refreshCaller;

  public ApiClient(SecureSession secureSession, Gson gson){
    this.secureSession = secureSession;
    this.gson = gson;
  }

  public void setRefreshCaller(Consumer<RefreshCallback> refreshCallback){
    this.refreshCaller = refreshCallback;
  }

  //builds url with any pararms/filters and the baseUrl being added
  private String buildUrl(ApiRequest request) throws UnsupportedEncodingException {

    String urlString = baseurl + request.getUrl();

    Map<String, String> params = request.getQueryParams();

    if (!params.isEmpty()) {
      StringBuilder query = new StringBuilder("?");

      boolean first = true;

      for (Map.Entry<String, String> param : params.entrySet()) {

        if (!first) {
          query.append("&");
        }

        //encoding done to reserve special characters
        query.append(
            URLEncoder.encode(param.getKey(), "UTF-8")
        );

        query.append("=");

        query.append(
            URLEncoder.encode(param.getValue(), "UTF-8")
        );

        first = false;
      }

      urlString += query;
    }

    return urlString;
  }

//acts as passer to executeRequest or refreshing first
  public <T> void execute(ApiRequest request, Type responseType, ApiCallback<T> callback){
    if(request.requiresAuthentication()){
      if (secureSession.isAccessTokenExpired()) {
        refreshCaller.accept(new RefreshCallback() {
          @Override
          public void onSuccess() {
            executeRequest(request,responseType,callback);
          }

          @Override
          public void onError(RefreshError error) {
            secureSession.setLoggedIn(false);
          }
        });

        return;
      }
    }

    executeRequest(request, responseType, callback);
  }

//does the request on a seperate thread, gets response, and returns to main thread
  public <T> void executeRequest(ApiRequest request, Type responseType, ApiCallback<T> callback){
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

  /**
   * Sends the request and maps the JSON envelope into an ApiResponse.
   * Steps: open connection -> write body -> read response -> parse.
   */
  private <T> ApiResponse<T> performRequest(ApiRequest request, Type responseType) throws Exception {
    HttpURLConnection connection = openConnection(request);
    try {
      writeBody(connection, request.getBody());
      int statusCode = connection.getResponseCode();
      String body = readResponse(connection, statusCode);
      return parseResponse(body, statusCode, responseType);
    } finally {
      connection.disconnect();
    }
  }

  // Creates the connection with method, headers, auth token and timeouts.
  private HttpURLConnection openConnection(ApiRequest request) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(buildUrl(request)).openConnection();
    connection.setRequestMethod(request.getMethod());

    if (request.requiresAuthentication()) {
      request.addAuthHeader(secureSession.getAccessToken());
    }
    for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
      connection.setRequestProperty(header.getKey(), header.getValue());
    }

    connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
    connection.setReadTimeout(READ_TIMEOUT_MS);
    return connection;
  }

  // Serialises the body to JSON and writes it, if there is one.
  private void writeBody(HttpURLConnection connection, Object body) throws IOException {
    if (body == null) return;
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);
    try (OutputStream output = connection.getOutputStream()) {
      output.write(gson.toJson(body).getBytes(StandardCharsets.UTF_8));
    }
  }

  //Reads the success or error stream into a String.
  private String readResponse(HttpURLConnection connection, int statusCode) throws IOException {
    InputStream stream = (statusCode >= 200 && statusCode < 300)
        ? connection.getInputStream()
        : connection.getErrorStream();
    if (stream == null) return "";   // some errors have no body

    StringBuilder body = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) body.append(line);
    }
    return body.toString();
  }

  // Maps the JSON envelope into ApiResponse<T>; rejects non-JSON (e.g. a PHP error page).
  private <T> ApiResponse<T> parseResponse(String body, int statusCode, Type responseType) throws IOException {
    ApiResponse<T> response;
    try {
      System.out.println(body);
      response = gson.fromJson(body, TypeToken.getParameterized(ApiResponse.class, responseType).getType());
      System.out.println(gson.toJson(response));
    } catch (JsonSyntaxException e) {
      System.out.println(e.getMessage());
      throw new IOException("Unexpected response from server (HTTP " + statusCode + ")", e);
    }

    if (response == null) {
      throw new IOException("Empty response from server (HTTP " + statusCode + ")");
    }
    response.setStatusCode(statusCode);
    System.out.println(statusCode);
    return response;
  }
}