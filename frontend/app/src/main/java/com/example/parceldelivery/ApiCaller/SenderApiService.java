package com.example.parceldelivery.ApiCaller;

import com.example.parceldelivery.Models.ReqCreateFeedbackModel;
import com.example.parceldelivery.Models.ReqCreateOrderModel;
import com.example.parceldelivery.Models.ResViewOrderModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SenderApiService {
    @POST("senderdetails/createOrder")
    Call<ApiResponse<String>> createOrder(@Header("Authorization") String bearerToken, @Body ReqCreateOrderModel order);

    @GET("senderdetails/getAllOrderInfo/{userId}")
    Call<ApiResponse<List<ResViewOrderModel>>> getAllOrderByUserId(@Header("Authorization") String bearerToken, @Path("userId") long userId);

    @GET("senderdetails/getOrderInfo/{orderId}")
    Call<ApiResponse<ResViewOrderModel>> getOrderInfoByOrderId(@Header("Authorization") String bearerToken, @Path("orderId") long orderId);

    @DELETE("senderdetails/deleteOrder/{orderId}")
    Call<ApiResponse<String>> deleteOrder(@Header("Authorization") String bearerToken, @Path("orderId") long orderId);

    @POST("senderdetails/createFeedback")
    Call<ApiResponse<String>> createFeedback(@Header("Authorization") String bearerToken, @Body ReqCreateFeedbackModel feedback);
}
