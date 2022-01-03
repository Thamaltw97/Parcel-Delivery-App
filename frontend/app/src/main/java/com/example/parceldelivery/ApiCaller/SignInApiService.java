package com.example.parceldelivery.ApiCaller;

import com.example.parceldelivery.Models.ReqLoginModel;
import com.example.parceldelivery.Models.ResViewLoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SignInApiService {
    @POST("auth/")
    Call<ApiResponse<ResViewLoginModel>> signIn(@Body ReqLoginModel login);
}
