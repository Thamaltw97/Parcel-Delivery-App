package com.example.parceldelivery.sender;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ReqCreateOrderModel;
import com.example.parceldelivery.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelDetailsActivity extends AppCompatActivity {

    EditText edit_receiver,edit_sender_Address,edit_receiver_address,edit_package_des,edit_rec_phone;
    RelativeLayout btn_send;
    private long userId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcel_details);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edit_receiver = findViewById(R.id.edit_receiver);
        edit_sender_Address = findViewById(R.id.edit_sender_Address);
        edit_receiver_address = findViewById(R.id.edit_receiver_address);
        edit_package_des = findViewById(R.id.edit_package_des);
        edit_rec_phone = findViewById(R.id.edit_rec_phone);
        btn_send = findViewById(R.id.btn_send);

        progressDialog = new ProgressDialog(ParcelDetailsActivity.this);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        userId = preferences.getLong(Constants.USERID_STORE, 0);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder(new ReqCreateOrderModel(edit_receiver.getText().toString(),
                        edit_rec_phone.getText().toString(),
                        edit_package_des.getText().toString(),
                        edit_receiver_address.getText().toString(),
                        edit_sender_Address.getText().toString(),
                        userId));
            }
        });

    }

    private void CreateOrder(ReqCreateOrderModel order) {
        showProgressBar("Create Order", "Please wait to create the order");

        SenderApiService retrofitAPI = ApiCaller.getClient().create(SenderApiService.class);
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<String>> call = retrofitAPI.createOrder(preferences.getString(Constants.TOKEN_STORE, ""), order);

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