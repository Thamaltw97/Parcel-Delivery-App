package com.example.parceldelivery.ApiCaller;

import com.example.parceldelivery.Models.ReqRegisterModel;
import com.example.parceldelivery.Models.ResUpdateUserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApiService {
    @POST("userdetails/register")
    Call<ApiResponse<ReqRegisterModel>> register(@Body ReqRegisterModel user);

    @GET("userdetails/getUserInfo/{userId}")
    Call<ApiResponse<ResUpdateUserModel>> getUserById(@Header("Authorization") String bearerToken, @Path("userId") long userId);

    @PUT("userdetails/updateInfo")
    Call<ApiResponse<String>> updateUserInfo(@Header("Authorization") String bearerToken, @Body ResUpdateUserModel user);
}
