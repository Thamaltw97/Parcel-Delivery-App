package com.example.parceldelivery.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SignInApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ReqLoginModel;
import com.example.parceldelivery.Models.ResViewLoginModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.admin.AdminHomeActivity;
import com.example.parceldelivery.rider.RiderHomeActivity;
import com.example.parceldelivery.sender.SenderHomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edt_user_name,edt_password;
    RelativeLayout btn_login;
    TextView tv_signup;
    private static String token;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edt_user_name = findViewById(R.id.edt_user_name);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        tv_signup = findViewById(R.id.tv_signup);

        progressDialog = new ProgressDialog(LoginActivity.this);

        String text = "<font color=#000000>Don't have an account?</font> <font color=#E29B12><b>SIGN UP</b></font>";
        tv_signup.setText(Html.fromHtml(text));

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(edt_user_name.getText().toString(), edt_password.getText().toString());
            }
        });

    }

    private void SignIn(String userName, String userPassword) {
        showProgressBar("Sign In", "Please wait to verify login details");
        SignInApiService retrofitAPI = ApiCaller.getClient().create(SignInApiService.class);
        Call<ApiResponse<ResViewLoginModel>> call = retrofitAPI.signIn(new ReqLoginModel(userName, userPassword));

        call.enqueue(new Callback<ApiResponse<ResViewLoginModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<ResViewLoginModel>> call, Response<ApiResponse<ResViewLoginModel>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    ApiResponse resApi = response.body();
                    ResViewLoginModel res = (ResViewLoginModel) resApi.getData();

                    if (resApi.isStatus()) {
                        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_PRIVATE);
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString(Constants.TOKEN_STORE, "Bearer " + res.getToken());
                        edit.putLong(Constants.USERID_STORE, res.getUserId());
                        edit.putString(Constants.USERNAME_STORE, res.getUserName());
                        edit.putString(Constants.USERTYPE_STORE, res.getUserType());
                        edit.putString(Constants.USER_STATUS_STORE, res.getUserStatus());
                        edit.commit();

                        if (res.getUserType().equals("A")) {
                            startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                        } else if (res.getUserType().equals("S")){
                            startActivity(new Intent(LoginActivity.this, SenderHomeActivity.class));
                        } else if (res.getUserType().equals("R")) {
                            startActivity(new Intent(LoginActivity.this, RiderHomeActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "User Type Invalid.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), resApi.getError(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ResViewLoginModel>> call, Throwable t) {
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