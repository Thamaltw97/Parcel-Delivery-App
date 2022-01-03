package com.example.parceldelivery.auth;

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

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.ApiCaller.UserApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ReqCreateOrderModel;
import com.example.parceldelivery.Models.ResUpdateUserModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.sender.OrderDetailsActivity;
import com.example.parceldelivery.sender.SenderHomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    TextView edit_userid_pro_up;
    EditText edit_username_pro_up, edit_email_pro_up, edit_phone_pro_up, edit_address_pro_up;
    RelativeLayout btn_update;
    private ProgressDialog progressDialog;

    String userid, username, email, phone, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        edit_userid_pro_up = findViewById(R.id.edit_userid_pro_up);
        edit_username_pro_up = findViewById(R.id.edit_username_pro_up);
        edit_email_pro_up = findViewById(R.id.edit_email_pro_up);
        edit_phone_pro_up = findViewById(R.id.edit_phone_pro_up);
        edit_address_pro_up = findViewById(R.id.edit_address_pro_up);
        btn_update = findViewById(R.id.btn_update);

        progressDialog = new ProgressDialog(UpdateProfileActivity.this);

        userid = "";
        username = "";
        email = "";
        phone = "";
        address = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            userid = extras.getString("userid");
            username = extras.getString("username");
            email = extras.getString("email");
            phone = extras.getString("phone");
            address = extras.getString("address");
        }

        edit_userid_pro_up.setText(userid);
        edit_username_pro_up.setText(username);
        edit_email_pro_up.setText(email);
        edit_phone_pro_up.setText(phone);
        edit_address_pro_up.setText(address);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                updateUser(new ResUpdateUserModel(Long.parseLong(userid),
                        edit_username_pro_up.getText().toString(),
                        edit_phone_pro_up.getText().toString(),
                        edit_email_pro_up.getText().toString(),
                        edit_address_pro_up.getText().toString()));
            }
        });

    }

    private void updateUser(ResUpdateUserModel user) {
        showProgressBar("Create Order", "Please wait to create the order");

        UserApiService retrofitAPI = ApiCaller.getClient().create(UserApiService.class);
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<String>> call = retrofitAPI.updateUserInfo(preferences.getString(Constants.TOKEN_STORE, ""), user);

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