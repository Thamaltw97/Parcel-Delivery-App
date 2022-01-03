package com.example.parceldelivery.rider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.RiderApiService;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ReqCreateOrderModel;
import com.example.parceldelivery.Models.ReqUpdateUserStatusModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.auth.MainActivity;
import com.example.parceldelivery.auth.ProfileActivity;
import com.example.parceldelivery.sender.ParcelDetailsActivity;
import com.example.parceldelivery.sender.SenderHomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiderHomeActivity extends AppCompatActivity {

    RelativeLayout
            btn_assigned_orders,
            btn_current_order, btn_com_orders;
    TextView tv_rider_profile, tv_rider_logout;
    SwitchCompat switchCompat;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_assigned_orders = findViewById(R.id.btn_assigned_orders);
        btn_current_order = findViewById(R.id.btn_current_order);
        btn_com_orders = findViewById(R.id.btn_com_orders);
        tv_rider_profile = findViewById(R.id.tv_rider_profile);
        tv_rider_logout = findViewById(R.id.tv_rider_logout);
        switchCompat = findViewById(R.id.btn_driver_switch);

        String text = "<font color=#E29B12><b>View</b></font> <font color=#000000>My Profile</font>";
        tv_rider_profile.setText(Html.fromHtml(text));

        progressDialog = new ProgressDialog(RiderHomeActivity.this);

        tv_rider_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderHomeActivity.this, ProfileActivity.class));
            }
        });

        tv_rider_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderHomeActivity.this, MainActivity.class));
            }
        });

        btn_assigned_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderHomeActivity.this, AssignedOrdersActivity.class));
            }
        });

        btn_com_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderHomeActivity.this, CompletedOrdersActivity.class));
            }
        });

        btn_current_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RiderHomeActivity.this, CurrentOrderActivity.class));
            }
        });

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        if (preferences.getString(Constants.USER_STATUS_STORE, "").equals("A")) {
            switchCompat.setChecked(true);
        } else {
            switchCompat.setChecked(false);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                    if (isChecked) {
//                        Toast.makeText(getApplicationContext(), "Driver Available!", Toast.LENGTH_SHORT).show();
                        updateRiderStatus("A");
                    }
                    else {
//                        Toast.makeText(getApplicationContext(), "Driver Unavailable!", Toast.LENGTH_SHORT).show();
                        updateRiderStatus("I");
                    }
            }
        });

    }

    private void updateRiderStatus(String userStatus) {
        showProgressBar("Change Status", "Please wait to change the status");

        RiderApiService retrofitAPI = ApiCaller.getClient().create(RiderApiService.class);
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        ReqUpdateUserStatusModel user = new ReqUpdateUserStatusModel(preferences.getLong(Constants.USERID_STORE, 0), userStatus);
        Call<ApiResponse<String>> call = retrofitAPI.updateRiderStatus(preferences.getString(Constants.TOKEN_STORE, ""), user);

        call.enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {

                    if (response.body().isStatus()) {
                        String res = response.body().getData();
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(ParcelDetailsActivity.this, SenderHomeActivity.class));
                        //finish();
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