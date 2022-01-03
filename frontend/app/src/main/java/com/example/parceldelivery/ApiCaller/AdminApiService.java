package com.example.parceldelivery.ApiCaller;

import com.example.parceldelivery.Models.ReqCreateApproveOrderModel;
import com.example.parceldelivery.Models.ReqCreateFeedbackModel;
import com.example.parceldelivery.Models.ReqRegisterModel;
import com.example.parceldelivery.Models.ResViewDiscountInfoModel;
import com.example.parceldelivery.Models.ResViewFeedbackModel;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.Models.ResViewUsersModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AdminApiService {
    @GET("admindetails/getAllUsers/")
    Call<ApiResponse<List<ResViewUsersModel>>> getAllUsers(@Header("Authorization") String bearerToken);

    @GET("admindetails/getAllFeedbacks/")
    Call<ApiResponse<List<ResViewFeedbackModel>>> getAllFeedbacks(@Header("Authorization") String bearerToken);

    @GET("admindetails/getAllOrders/")
    Call<ApiResponse<List<ResViewOrderModel>>> getAllOrders(@Header("Authorization") String bearerToken);

    @GET("admindetails/getDiscountInfo/{orderId}")
    Call<ApiResponse<ResViewDiscountInfoModel>> getDiscountInfoByOrderId(@Header("Authorization") String bearerToken, @Path("orderId") long orderId);

    @POST("admindetails/approveOrder")
    Call<ApiResponse<String>> approveOrder(@Header("Authorization") String bearerToken, @Body ReqCreateApproveOrderModel order);
}
