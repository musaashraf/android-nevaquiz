package com.nevadiatechnology.nevaquiz.Retrofit;

import com.nevadiatechnology.nevaquiz.Model.FCMData;
import com.nevadiatechnology.nevaquiz.Model.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FCMApi {
    @Headers({"Authorization: key=AIzaSyD0hB2AnA9YwdxTgaYw1jIqGgwqK-TjBtU", "Content-Type:application/json"})
    @POST("fcm/send")
    Call<FCMResponse> sendNotification(@Body FCMData body);
}
