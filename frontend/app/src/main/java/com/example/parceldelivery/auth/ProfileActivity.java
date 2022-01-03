package com.example.parceldelivery.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.ApiCaller.UserApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ResUpdateUserModel;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.sender.OrderDetailsActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    TextView tv_userid_profile, tv_username_profile, tv_email_profile, tv_phone_profile, tv_address_profile;
    RelativeLayout btn_update_profile;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv_userid_profile = findViewById(R.id.tv_userid_profile);
        tv_username_profile = findViewById(R.id.tv_username_profile);
        tv_email_profile = findViewById(R.id.tv_email_profile);
        tv_phone_profile = findViewById(R.id.tv_phone_profile);
        tv_address_profile = findViewById(R.id.tv_address_profile);
        btn_update_profile = findViewById(R.id.btn_update_profile);

        progressDialog = new ProgressDialog(ProfileActivity.this);

        setOrderInfo();

//        tv_userid_profile.setText("001");
//        tv_username_profile.setText("Thamalwije");
//        tv_email_profile.setText("thamal@gmail.com");
//        tv_phone_profile.setText("+94712963564");
//        tv_address_profile.setText("41/1 A, Saddhasena Mawatha, Maha Ambalangoda, Ambalangoda");

        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, UpdateProfileActivity.class));
                Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                intent.putExtra("userid", tv_userid_profile.getText());
                intent.putExtra("username", tv_username_profile.getText());
                intent.putExtra("email", tv_email_profile.getText());
                intent.putExtra("phone", tv_phone_profile.getText());
                intent.putExtra("address", tv_address_profile.getText());
                startActivity(intent);
            }
        });

    }

    private void setOrderInfo() {
        getUserById();
    }

    private void getUserById() {

        showProgressBar("Getting order details", "Please wait....");

        UserApiService retrofitAPI = ApiCaller.getClient().create(UserApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<ResUpdateUserModel>> call = retrofitAPI.getUserById(preferences.getString(Constants.TOKEN_STORE, ""), preferences.getLong(Constants.USERID_STORE, 0));

        call.enqueue(new Callback<ApiResponse<ResUpdateUserModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResUpdateUserModel>> call, Response<ApiResponse<ResUpdateUserModel>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        //ApiResponse resApi = response.body();
                        tv_userid_profile.setText(response.body().getData().getUserId() + "");
                        tv_username_profile.setText(response.body().getData().getUserName());
                        tv_email_profile.setText(response.body().getData().getUserEmail());
                        tv_phone_profile.setText(response.body().getData().getUserMobile());
                        tv_address_profile.setText(response.body().getData().getUserAddress());
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResUpdateUserModel>> call, Throwable t) {
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
                        //ApiResponse resApi = response.body();
//                        tv_name_my_order_details.setText(response.body().getData().getReceiverName());
//                        tv_id_my_order_details.setText(response.body().getData().getOrderId() + "");
//                        tv_address_my_order_details.setText(response.body().getData().getReceiveLocation());
//                        tv_pick_my_order_details.setText(response.body().getData().getPickupLocation());
//                        tv_pick_phone_my_order_details.setText(response.body().getData().getReceiverMobile());
//                        tv_desc_my_order_details.setText(response.body().getData().getDescription());
//                        tv_status_my_order_details.setText(response.body().getData().getStatus().equals("P") ? "Pending" : response.body().getData().getStatus().equals("A") ? "Approved" : response.body().getData().getStatus().equals("S") ? "Shipped" : "Delivered");
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