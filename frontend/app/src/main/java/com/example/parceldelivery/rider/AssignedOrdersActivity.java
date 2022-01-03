package com.example.parceldelivery.rider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.parceldelivery.ApiCaller.AdminApiService;
import com.example.parceldelivery.ApiCaller.ApiCaller;
import com.example.parceldelivery.ApiCaller.ApiResponse;
import com.example.parceldelivery.ApiCaller.RiderApiService;
import com.example.parceldelivery.ApiCaller.SenderApiService;
import com.example.parceldelivery.Constants.Constants;
import com.example.parceldelivery.Models.ReqCreateApproveOrderModel;
import com.example.parceldelivery.Models.ReqUpdateOrderStatusModel;
import com.example.parceldelivery.Models.ResViewOrderModel;
import com.example.parceldelivery.R;
import com.example.parceldelivery.adapters.AssignedOrdersRecycleAdapter;
import com.example.parceldelivery.adapters.MyOrdersRecycleAdapter;
import com.example.parceldelivery.adapters.PendingOrdersRecycleAdapter;
import com.example.parceldelivery.admin.PendingOrder;
import com.example.parceldelivery.sender.MyOrder;
import com.example.parceldelivery.sender.MyOrdersActivity;
import com.example.parceldelivery.sender.OrderDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignedOrdersActivity extends AppCompatActivity {

    private ArrayList<PendingOrder> orderList;
    private RecyclerView recyclerView;
    private PendingOrdersRecycleAdapter.PendingOrdersRecycleViewClickListner listener;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_orders);

        recyclerView = findViewById(R.id.assignedOrdersRecycleView);
        orderList = new ArrayList<>();

        progressDialog = new ProgressDialog(AssignedOrdersActivity.this);

        setOrderInfo();
        setAdapter();

    }

    private void setAdapter() {
        setOnClickListener();
        PendingOrdersRecycleAdapter adapter = new PendingOrdersRecycleAdapter(orderList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = new PendingOrdersRecycleAdapter.PendingOrdersRecycleViewClickListner() {
            @Override
            public void onClick(View v, int position) {
                openDialog(orderList.get(position).getOrderDesc(), orderList.get(position).getOrderAddress(), orderList.get(position).getOrderId());
            }
        };
    }

    private void setOrderInfo() {
        GetAllOrderByUserId();
    }

    private void GetAllOrderByUserId() {

        showProgressBar("Assigned Orders", "Please wait....");

        RiderApiService retrofitAPI = ApiCaller.getClient().create(RiderApiService.class);

        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
        Call<ApiResponse<List<ResViewOrderModel>>> call = retrofitAPI.getOrderById(preferences.getString(Constants.TOKEN_STORE, ""), preferences.getLong(Constants.USERID_STORE, 0));

        call.enqueue(new Callback<ApiResponse<List<ResViewOrderModel>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ResViewOrderModel>>> call, Response<ApiResponse<List<ResViewOrderModel>>> response) {
                hideProgressBar();
                if(response.isSuccessful()) {
                    if (response.body().isStatus()){
                        for(ResViewOrderModel order : response.body().getData()) {
                            if (order.getStatus().equals("A")) {
                                orderList.add(new PendingOrder((order.getOrderId() + ""), order.getDescription(), "Receiver : " + order.getReceiverName(), order.getStatus().equals("P") ? "Pending" : order.getStatus().equals("A") ? "Approved" : order.getStatus().equals("S") ? "Shipped" : "Delivered"));
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getError(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
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

    public void openDialog(String description, String address, String orderId){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(address)
                .setCancelable(false)
                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setNegativeButton("GET ORDER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        showProgressBar("Get Order", "Please wait to get the order");

                        RiderApiService retrofitAPI = ApiCaller.getClient().create(RiderApiService.class);
                        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFERENCE, MODE_WORLD_READABLE);
                        ReqUpdateOrderStatusModel order = new ReqUpdateOrderStatusModel(Long.parseLong(orderId), "S");
                        Call<ApiResponse<String>> call = retrofitAPI.updateOrderStatus(preferences.getString(Constants.TOKEN_STORE, ""), order);

                        call.enqueue(new Callback<ApiResponse<String>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                                hideProgressBar();
                                if(response.isSuccessful()) {
                                    String res = response.body().getData();
                                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                                    finish();
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
                });

        AlertDialog alert = builder.create();
        alert.setTitle(description);
        alert.show();

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