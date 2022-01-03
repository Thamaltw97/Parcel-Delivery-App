package com.example.parceldelivery.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.parceldelivery.R;
import com.example.parceldelivery.auth.MainActivity;
import com.example.parceldelivery.sender.ParcelDetailsActivity;
import com.example.parceldelivery.sender.SenderHomeActivity;

public class AdminHomeActivity extends AppCompatActivity {

    RelativeLayout btn_orders_admin, btn_app_orders_admin, btn_users_admin, btn_feedbacks_admin;
    TextView tv_admin_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        btn_orders_admin = findViewById(R.id.btn_orders_admin);
        btn_app_orders_admin = findViewById(R.id.btn_app_orders_admin);
        btn_users_admin = findViewById(R.id.btn_users_admin);
        btn_feedbacks_admin = findViewById(R.id.btn_feedbacks_admin);
        tv_admin_logout = findViewById(R.id.tv_admin_logout);

        btn_orders_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, PendingOrdersActivity.class));
            }
        });

        btn_app_orders_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, OtherOrdersActivity.class));
            }
        });

        btn_users_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, UsersActivity.class));
            }
        });

        btn_feedbacks_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, FeedbackActivity.class));
            }
        });

        tv_admin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
            }
        });

    }
}