package com.muchlish.scan_ai.service;

import com.muchlish.scan_ai.activity.entity.Auth;
import com.muchlish.scan_ai.activity.entity.MyResponse;
import com.muchlish.scan_ai.activity.entity.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/auth")
    Call<MyResponse<Auth>> login(@Body User user);

    @GET("/auth/account")
    Call<MyResponse<User>> getAccount(@Header("Authorization") String token);
}
