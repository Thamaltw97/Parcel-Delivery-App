package com.example.parceldelivery.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.AdminApiService;
import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ReqCreateApproveOrderModel;
import com.example.parceldelivery.Models.ReqCreateOrderModel;
import com.example.parceldelivery.Models.ResViewDiscountInfoModel;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.sender.ParcelDetailsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveOrderWithPriceActivity extends AppCompatActivity {

    TextView tv_name_approve_order_details, tv_userid_approve_order_details,
            tv_order_id_approve_order_details, tv_send_address_approve_order_details,
            tv_receive_address_approve_order_details, tv_receive_phone_approve_order_details,
            tv_desc_approve_order_details, tv_status_approve_order_details, tv_driver_name_approve_order_details,
            tv_driver_id_approve_order_details, tv_loyalty_lvl_approve_order_details,
            tv_discount_approve_order_details;
    EditText edit_price_approve_order_details;
    RelativeLayout btn_approve_order;
    private ProgressDialog progressDialog;

    long orderId, driverId, userId;
    String driverName;
    String strApprovalStatus = "A";
    Double intDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_order_with_price);

        tv_name_approve_order_details = findViewById(R.id.tv_name_approve_order_details);
        tv_userid_approve_order_details = findViewById(R.id.tv_userid_approve_order_details);
        tv_order_id_approve_order_details = findViewById(R.id.tv_order_id_approve_order_details);
        tv_send_address_approve_order_details = findViewById(R.id.tv_send_address_approve_order_details);
        tv_receive_address_approve_order_details = findViewById(R.id.tv_receive_address_approve_order_details);
        tv_receive_phone_approve_order_details = findViewById(R.id.tv_receive_phone_approve_order_details);
        tv_desc_approve_order_details = findViewById(R.id.tv_desc_approve_order_details);
        tv_status_approve_order_details = findViewById(R.id.tv_status_approve_order_details);
        tv_driver_name_approve_order_details = findViewById(R.id.tv_driver_name_approve_order_details);
        tv_driver_id_approve_order_details = findViewById(R.id.tv_driver_id_approve_order_details);
        tv_loyalty_lvl_approve_order_details = findViewById(R.id.tv_loyalty_lvl_approve_order_details);
        tv_discount_approve_order_details = findViewById(R.id.tv_discount_approve_order_details);

        edit_price_approve_order_details = findViewById(R.id.edit_price_approve_order_details);

        btn_approve_order = findViewById(R.id.btn_approve_order);

        progressDialog = new ProgressDialog(ApproveOrderWithPriceActivity.this);

        orderId = 0;
        driverId = 0;
        driverName= "";
        intDiscount = 0.00;

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            orderId = Long.parseLong(extras.getString("orderId"));
            driverId = Long.parseLong(extras.getString("driverId"));
            driverName = extras.getString("driverName");
        }
        tv_order_id_approve_order_details.setText(("Order ID : " + orderId));
        tv_driver_id_approve_order_details.setText(("Driver ID : " + driverId));
        tv_driver_name_approve_order_details.setText(("Driver Name : " + driverName));

        setOrderInfo(orderId);

        btn_approve_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ApproveOrderWithPriceActivity.this, OtherOrdersActivity.class));

                Double intFinalPrice = (Double.parseDouble(edit_price_approve_order_details.getText().toString()) - (Double.parseDouble(edit_price_approve_order_details.getText().toString()) * intDiscount));

                ApproveOrder(new ReqCreateApproveOrderModel(strApprovalStatus,
                        intFinalPrice,
                        "",
                        orderId,
                        userId,
                        driverId));

                //finish();
            }
        });

    }

    private void ApproveOrder(ReqCreateApproveOrderModel order) {
        showProgressBar("Approve Order", "Please wait to approve the order");

        AdminApiService retrofitAPI = ApiCaller.getClient().create(AdminApiService.class);
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<String>> call = retrofitAPI.approveOrder(preferences.getString(Constants.TOKEN_STORE, ""), order);

        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        String res = response.body().getData();
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(ParcelDetailsActivity.this, SenderHomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setOrderInfo(long orderId) {
        getOrderInfoByOrderId(orderId);
    }

    private void getOrderInfoByOrderId(long orderId) {

        showProgressBar("Getting order details", "Please wait....");

        SenderApiService retrofitAPI = ApiCaller.getClient().create(SenderApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<ResViewOrderModel>> call = retrofitAPI.getOrderInfoByOrderId(preferences.getString(Constants.TOKEN_STORE, ""), orderId);

        call.enqueue(new Callback<ApiResponse<ResViewOrderModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResViewOrderModel>> call, Response<ApiResponse<ResViewOrderModel>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        //ApiResponse resApi = response.body();
                        tv_name_approve_order_details.setText(("Name : " + response.body().getData().getReceiverName()));
                        //tv_id_my_order_details.setText(response.body().getData().getOrderId() + "");
                        tv_send_address_approve_order_details.setText(("Sender Addr. : " + response.body().getData().getPickupLocation()));
                        tv_receive_address_approve_order_details.setText(("Receiv. Addr. : " + response.body().getData().getReceiveLocation()));
                        tv_receive_phone_approve_order_details.setText(("Mobile : " + response.body().getData().getReceiverMobile()));
                        tv_desc_approve_order_details.setText(("Desc. : " + response.body().getData().getDescription()));
                        tv_status_approve_order_details.setText(("Status : " + (response.body().getData().getStatus().equals("P") ? "Pending" : response.body().getData().getStatus().equals("A") ? "Approved" : response.body().getData().getStatus().equals("S") ? "Shipped" : "Delivered")));

                        getDiscountInfo(orderId);

                    } else {
                        Toast.makeText(getApplicationContext(), "Unknown error.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResViewOrderModel>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getDiscountInfo(long orderId) {

        showProgressBar("Getting order details", "Please wait....");

        AdminApiService retrofitAPI = ApiCaller.getClient().create(AdminApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<ResViewDiscountInfoModel>> call = retrofitAPI.getDiscountInfoByOrderId(preferences.getString(Constants.TOKEN_STORE, ""), orderId);

        call.enqueue(new Callback<ApiResponse<ResViewDiscountInfoModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResViewDiscountInfoModel>> call, Response<ApiResponse<ResViewDiscountInfoModel>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    tv_userid_approve_order_details.setText(("User ID : " + response.body().getData().getUserId()));
                    userId = response.body().getData().getUserId();

                    if (response.body().getData().getUserLevel() == 0) {
                        intDiscount = 0.00;
                        tv_loyalty_lvl_approve_order_details.setText(("Loyalty level : 0"));
                        tv_discount_approve_order_details.setText(("Discount : 0%"));
                    } else if (response.body().getData().getUserLevel() == 1) {
                        intDiscount = 0.05;
                        tv_loyalty_lvl_approve_order_details.setText(("Loyalty level : 1"));
                        tv_discount_approve_order_details.setText(("Discount : 5%"));
                    } else if (response.body().getData().getUserLevel() == 2) {
                        intDiscount = 0.1;
                        tv_loyalty_lvl_approve_order_details.setText(("Loyalty level : 2"));
                        tv_discount_approve_order_details.setText(("Discount : 10%"));
                    } else if (response.body().getData().getUserLevel() == 3) {
                        intDiscount = 0.15;
                        tv_loyalty_lvl_approve_order_details.setText(("Loyalty level : 3"));
                        tv_discount_approve_order_details.setText(("Discount : 15%"));
                    } else if (response.body().getData().getUserLevel() == 4) {
                        intDiscount = 0.2;
                        tv_loyalty_lvl_approve_order_details.setText(("Loyalty level : 4"));
                        tv_discount_approve_order_details.setText(("Discount : 20%"));
                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResViewDiscountInfoModel>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

//        call.enqueue(new Callback<ApiResponse<ResViewOrderModel>>() {
//            @Override
//            public void onResponse(Call<ApiResponse<ResViewOrderModel>> call, Response<ApiResponse<ResViewOrderModel>> response) {
//                hideProgressBar();
//                if(response.isSuccessful()) {
//                    if (response.body().isStatus()) {
//                        //ApiResponse resApi = response.body();
//                        tv_name_approve_order_details.setText(("Name : " + response.body().getData().getReceiverName()));
//                        //tv_id_my_order_details.setText(response.body().getData().getOrderId() + "");
//                        tv_send_address_approve_order_details.setText(("Sender Addr. : " + response.body().getData().getPickupLocation()));
//                        tv_receive_address_approve_order_details.setText(("Receiv. Addr. : " + response.body().getData().getReceiveLocation()));
//                        tv_receive_phone_approve_order_details.setText(("Mobile : " + response.body().getData().getReceiverMobile()));
//                        tv_desc_approve_order_details.setText(("Desc. : " + response.body().getData().getDescription()));
//                        tv_status_approve_order_details.setText(("Status : " + (response.body().getData().getStatus().equals("P") ? "Pending" : response.body().getData().getStatus().equals("A") ? "Approved" : response.body().getData().getStatus().equals("S") ? "Shipped" : "Delivered")));
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Unknown error.", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse<ResViewOrderModel>> call, Throwable t) {
//                hideProgressBar();
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

    }

    private void showProgressBar(String title, String desc) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(desc);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressBar() {
        progressDialog.dismiss();
    }

}