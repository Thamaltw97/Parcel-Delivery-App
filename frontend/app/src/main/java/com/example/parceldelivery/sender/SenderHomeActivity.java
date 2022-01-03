package com.example.parceldelivery.sender;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parceldelivery.R;
import com.example.parceldelivery.auth.LoginActivity;
import com.example.parceldelivery.auth.MainActivity;
import com.example.parceldelivery.auth.ProfileActivity;
import com.example.parceldelivery.auth.RegisterActivity;

public class SenderHomeActivity extends AppCompatActivity {

    RelativeLayout btn_send,btn_orders;
    TextView tv_sender_profile, tv_sender_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_send = findViewById(R.id.btn_send);
        btn_orders = findViewById(R.id.btn_orders);
        tv_sender_profile = findViewById(R.id.tv_sender_profile);
        tv_sender_logout = findViewById(R.id.tv_sender_logout);

        String text = "<font color=#E29B12><b>View</b></font> <font color=#000000>My Profile</font>";
        tv_sender_profile.setText(Html.fromHtml(text));

        tv_sender_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SenderHomeActivity.this, ProfileActivity.class));
            }
        });

        tv_sender_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SenderHomeActivity.this, MainActivity.class));
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SenderHomeActivity.this, ParcelDetailsActivity.class));
            }
        });

        btn_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SenderHomeActivity.this, MyOrdersActivity.class));
            }
        });

    }
}