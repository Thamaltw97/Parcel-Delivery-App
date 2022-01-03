package com.example.parceldelivery.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.UserApiService;
import com.example.parceldelivery.Models.ReqRegisterModel;
import com.example.parceldelivery.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_user_name,edt_user_email,edt_password,edt_phone,edt_address;
    RelativeLayout btn_signup;
    TextView tv_login;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edt_user_name = findViewById(R.id.edt_user_name);
        edt_user_email = findViewById(R.id.edt_user_email);
        edt_password = findViewById(R.id.edt_password);
        edt_phone = findViewById(R.id.edt_phone);
        edt_address = findViewById(R.id.edt_address);
        btn_signup = findViewById(R.id.btn_signup);
        tv_login = findViewById(R.id.tv_login);

        progressDialog = new ProgressDialog(RegisterActivity.this);

        String text = "<font color=#000000>Already have an account?</font> <font color=#E29B12><b>LOGIN</b></font>";
        tv_login.setText(Html.fromHtml(text));

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register(new ReqRegisterModel(edt_user_name.getText().toString(), edt_password.getText().toString(), edt_phone.getText().toString(),
                        edt_user_email.getText().toString(), edt_address.getText().toString(), "S"));
            }
        });
    }

    private void Register(ReqRegisterModel user) {
        showProgressBar("Register User", "Please wait to register details");
        UserApiService retrofitAPI = ApiCaller.getClient().create(UserApiService.class);
        Call<ApiResponse<ReqRegisterModel>> call = retrofitAPI.register(user);

        call.enqueue(new Callback<ApiResponse<ReqRegisterModel>>() {
            @Override
            public void onResponse(Call<ApiResponse<ReqRegisterModel>> call, Response<ApiResponse<ReqRegisterModel>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    ReqRegisterModel user = response.body().getData();
                    Toast.makeText(getApplicationContext(), "Registered : " + user.getUserName() .toString(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ReqRegisterModel>> call, Throwable t) {
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