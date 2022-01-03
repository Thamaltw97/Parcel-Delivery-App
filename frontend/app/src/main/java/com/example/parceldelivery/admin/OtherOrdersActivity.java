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
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.AdminApiService;
import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.adapters.AssignedOrdersRecycleAdapter;
import com.example.parceldelivery.adapters.PendingOrdersRecycleAdapter;
import com.example.parceldelivery.rider.AssignedOrder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherOrdersActivity extends AppCompatActivity {

    private ArrayList<AssignedOrder> orderList;
    private RecyclerView recyclerView;
    private AssignedOrdersRecycleAdapter.AssignedOrdersRecycleViewClickListner listener;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_orders);

        recyclerView = findViewById(R.id.otherOrdersRecycleView);
        orderList = new ArrayList<>();

        progressDialog = new ProgressDialog(OtherOrdersActivity.this);

        setOrderInfo();
        setAdapter();

    }

    private void setAdapter() {
        setOnClickListener();
        AssignedOrdersRecycleAdapter adapter = new AssignedOrdersRecycleAdapter(orderList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOrderInfo() {
//        orderList.add(new AssignedOrder("001", "Macbook Pro 2019", "Address : Ambalangoda", "Delivered"));
//        orderList.add(new AssignedOrder("001", "Iphone 13 pro max", "Address : Malabe", "Shipped"));
//        orderList.add(new AssignedOrder("001", "IWatch series 7", "Address : Kurunegala", "Approved"));
        getAllPendingOrders();
    }

    private void getAllPendingOrders() {

        showProgressBar("Other Orders", "Please wait....");

        AdminApiService retrofitAPI = ApiCaller.getClient().create(AdminApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<List<ResViewOrderModel>>> call = retrofitAPI.getAllOrders(preferences.getString(Constants.TOKEN_STORE, ""));

        call.enqueue(new Callback<ApiResponse<List<ResViewOrderModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ResViewOrderModel>>> call, Response<ApiResponse<List<ResViewOrderModel>>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()){
                        for(ResViewOrderModel order : response.body().getData()) {
                            if (!order.getStatus().equals("P")) {
                                orderList.add(new AssignedOrder((order.getOrderId() + ""), order.getDescription(), order.getReceiveLocation(), (order.getStatus().equals("A") ? "Approved" : order.getStatus().equals("S") ? "Shipped" : "Delivered")));
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }
                }
                setAdapter();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ResViewOrderModel>>> call, Throwable t) {
                hideProgressBar();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setOnClickListener() {
        listener = new AssignedOrdersRecycleAdapter.AssignedOrdersRecycleViewClickListner() {
            @Override
            public void onClick(View v, int position) {
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