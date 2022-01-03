package com.example.parceldelivery.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.AdminApiService;
import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.Models.ResViewUsersModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.adapters.AssignDriverRecycleAdapter;
import com.example.parceldelivery.adapters.PendingOrdersRecycleAdapter;
import com.example.parceldelivery.sender.MyOrder;
import com.example.parceldelivery.sender.MyOrdersActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignDriverActivity extends AppCompatActivity {

    private ArrayList<AssignDriver> driverList;
    private RecyclerView recyclerView;
    private AssignDriverRecycleAdapter.AssignDriverRecycleViewClickListner listener;

    TextView edit_order_id_assign_driver;
    String orderId;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_driver);

        edit_order_id_assign_driver = findViewById(R.id.edit_order_id_assign_driver);
        recyclerView = findViewById(R.id.assignDriverRecycleView);
        driverList = new ArrayList<>();

        progressDialog = new ProgressDialog(AssignDriverActivity.this);

        orderId = "";

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            orderId = extras.getString("orderId");
        }
        edit_order_id_assign_driver.setText(orderId);

        setDriverInfo();
        setAdapter();

    }

    private void setAdapter() {
        setOnClickListener();
        AssignDriverRecycleAdapter adapter = new AssignDriverRecycleAdapter(driverList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setDriverInfo() {
        getAllUsers();
    }

    private void getAllUsers() {

        showProgressBar("All Users", "Please wait....");

        AdminApiService retrofitAPI = ApiCaller.getClient().create(AdminApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<List<ResViewUsersModel>>> call = retrofitAPI.getAllUsers(preferences.getString(Constants.TOKEN_STORE, ""));

        call.enqueue(new Callback<ApiResponse<List<ResViewUsersModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ResViewUsersModel>>> call, Response<ApiResponse<List<ResViewUsersModel>>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        for(ResViewUsersModel user : response.body().getData()) {
                            if (user.getUserType().equals("R")) {
                                driverList.add(new AssignDriver(user.getUserId(), user.getUserName(), user.getUserStatus().equals("A") ? "Available" : "Unavailable"));
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }
                }
                setAdapter();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ResViewUsersModel>>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setOnClickListener() {
        listener = new AssignDriverRecycleAdapter.AssignDriverRecycleViewClickListner() {
            @Override
            public void onClick(View v, int position) {

                //----- TODO Change the Activity class -----------------------------------------

                if (driverList.get(position).getDriverStatus().equals("Unavailable")) {
                    Toast.makeText(getApplicationContext(), "Driver is not available.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ApproveOrderWithPriceActivity.class);
                    intent.putExtra("orderId", edit_order_id_assign_driver.getText());
                    intent.putExtra("driverId", driverList.get(position).getDriverId() + "");
                    intent.putExtra("driverName", driverList.get(position).getDriverName());
                    startActivity(intent);
                }
            }
        };
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