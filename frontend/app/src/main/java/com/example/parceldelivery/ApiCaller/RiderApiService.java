package com.example.parceldelivery.ApiCaller;

import com.example.parceldelivery.Models.ReqCreateFeedbackModel;
import com.example.parceldelivery.Models.ReqUpdateOrderStatusModel;
import com.example.parceldelivery.Models.ReqUpdateUserStatusModel;
import com.example.parceldelivery.Models.ResViewOrderModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RiderApiService {

    @PUT("riderdetails/updateRiderStatus/")
    Call<ApiResponse<String>> updateRiderStatus(@Header("Authorization") String bearerToken, @Body ReqUpdateUserStatusModel user);

    @PUT("riderdetails/updateOrderStatus/")
    Call<ApiResponse<String>> updateOrderStatus(@Header("Authorization") String bearerToken, @Body ReqUpdateOrderStatusModel order);

    @GET("riderdetails/getAllOrder/{userId}")
    Call<ApiResponse<List<ResViewOrderModel>>> getOrderById(@Header("Authorization") String bearerToken, @Path("userId") long userId);

}
