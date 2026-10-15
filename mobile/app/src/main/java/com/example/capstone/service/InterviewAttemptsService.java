
package com.example.capstone.service;

import com.example.capstone.domainModels.UserInterviewAttempt;
import com.example.capstone.domainModels.UserInterviewAttemptSummary;
import com.example.capstone.model.request.AnalyseRequest;
import com.example.capstone.model.request.DeleteAttemptRequest;
import com.example.capstone.model.request.SetAttemptToReviewedRequest;
import com.example.capstone.model.request.SubmitInterviewAttemptRequest;
import com.example.capstone.model.response.SubmitAttemptResponse;
import com.example.capstone.network.ApiCallback;
import com.example.capstone.network.ApiClient;
import com.example.capstone.network.ApiRequest;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class InterviewAttemptsService {

  private final ApiClient apiClient;

  public InterviewAttemptsService(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  // DELETE: Delete all interview attempts
  public void deleteAllInterviewAttempts(
          ApiCallback<Void> callback) {

    ApiRequest request = new ApiRequest(
            "interviewAttempt/deleteAttempts"
    );

    request.setMethodDELETE();
    request.setRequiresAuthentication(true);

    apiClient.execute(request, Void.class, callback);
  }

  // DELETE: Delete one interview attempt
  public void deleteInterviewAttempt(
          DeleteAttemptRequest deleteAttemptRequest,
          ApiCallback<Void> callback) {

    ApiRequest request = new ApiRequest(
            "interviewAttempt/deleteAttempt"
    );

    request.setMethodDELETE();
    request.setRequiresAuthentication(true);
    request.setBody(deleteAttemptRequest);

    apiClient.execute(request, Void.class, callback);
  }

  // POST: Submit interview attempt
  public void submitInterviewAttempt(
          SubmitInterviewAttemptRequest attemptRequest,
          ApiCallback<SubmitAttemptResponse> callback) {

    ApiRequest request = new ApiRequest(
            "interviewAttempt/submitAttempt"
    );

    request.setMethodPost();
    request.setRequiresAuthentication(true);
    request.setBody(attemptRequest);

    apiClient.execute(
            request,
            SubmitAttemptResponse.class,
            callback
    );
  }

  // Awaiting backend endpoint confirmation
  public void requestAnalysis(
          AnalyseRequest analyseRequest,
          ApiCallback<Void> callback) {

    callback.onGeneralError(
            new UnsupportedOperationException(
                    "Analysis endpoint not configured"
            )
    );
  }

  // GET: Retrieve a specific interview attempt
  public void getInterviewAttempt(
          int interviewAttemptId,
          ApiCallback<UserInterviewAttempt> callback) {

    ApiRequest request = new ApiRequest(
            "interviewAttempt/getAttempt"
    );

    request.setMethodGET();
    request.setRequiresAuthentication(true);
    request.addParams(
            "id",
            String.valueOf(interviewAttemptId)
    );

    apiClient.execute(
            request,
            UserInterviewAttempt.class,
            callback
    );
  }

  // GET: Retrieve interview attempts with sorting
  public void getInterviewAttempts(
          String sortBy,
          String sortOrder,
          ApiCallback<List<UserInterviewAttemptSummary>> callback) {

    ApiRequest request = new ApiRequest(
            "interviewAttempt/getAttempts"
    );

    request.setMethodGET();
    request.setRequiresAuthentication(true);

    if (sortBy != null && !sortBy.isEmpty()) {
      request.addParams("sortBy", sortBy);
    }

    if (sortOrder != null && !sortOrder.isEmpty()) {
      request.addParams("sortOrder", sortOrder);
    }

    Type type = new TypeToken<
            List<UserInterviewAttemptSummary>>() {}.getType();

    apiClient.execute(request, type, callback);
  }

  // PATCH: Mark an attempt as reviewed
  public void setToReviewed(
          SetAttemptToReviewedRequest setAttemptToReviewedRequest,
          ApiCallback<Void> callback) {

    ApiRequest request = new ApiRequest(
            "interviewAttempt/setToReviewed"
    );

    request.setMethodPATCH();
    request.setRequiresAuthentication(true);
    request.setBody(setAttemptToReviewedRequest);

    apiClient.execute(request, Void.class, callback);
  }
}
